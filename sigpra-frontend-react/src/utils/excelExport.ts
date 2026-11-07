const escapeXml = (value: string) => value
  .replace(/&/g, "&amp;")
  .replace(/</g, "&lt;")
  .replace(/>/g, "&gt;")
  .replace(/"/g, "&quot;")
  .replace(/'/g, "&apos;");

const columnName = (column: number) => {
  let name = "";
  let index = column;
  while (index > 0) {
    const remainder = (index - 1) % 26;
    name = String.fromCharCode(65 + remainder) + name;
    index = Math.floor((index - 1) / 26);
  }
  return name;
};

const crc32 = (bytes: Uint8Array) => {
  let crc = 0xffffffff;
  for (const byte of bytes) {
    crc ^= byte;
    for (let bit = 0; bit < 8; bit += 1) {
      crc = (crc >>> 1) ^ (crc & 1 ? 0xedb88320 : 0);
    }
  }
  return (crc ^ 0xffffffff) >>> 0;
};

const write16 = (view: DataView, offset: number, value: number) => view.setUint16(offset, value, true);
const write32 = (view: DataView, offset: number, value: number) => view.setUint32(offset, value, true);

const createZip = (files: Array<{ name: string; content: string }>) => {
  const encoder = new TextEncoder();
  const localParts: Uint8Array[] = [];
  const centralParts: Uint8Array[] = [];
  let localOffset = 0;

  files.forEach(file => {
    const name = encoder.encode(file.name);
    const content = encoder.encode(file.content);
    const checksum = crc32(content);

    const localHeader = new Uint8Array(30 + name.length);
    const localView = new DataView(localHeader.buffer);
    write32(localView, 0, 0x04034b50);
    write16(localView, 4, 20);
    write16(localView, 6, 0x0800);
    write16(localView, 8, 0);
    write32(localView, 14, checksum);
    write32(localView, 18, content.length);
    write32(localView, 22, content.length);
    write16(localView, 26, name.length);
    localHeader.set(name, 30);
    localParts.push(localHeader, content);

    const centralHeader = new Uint8Array(46 + name.length);
    const centralView = new DataView(centralHeader.buffer);
    write32(centralView, 0, 0x02014b50);
    write16(centralView, 4, 20);
    write16(centralView, 6, 20);
    write16(centralView, 8, 0x0800);
    write16(centralView, 10, 0);
    write32(centralView, 16, checksum);
    write32(centralView, 20, content.length);
    write32(centralView, 24, content.length);
    write16(centralView, 28, name.length);
    write32(centralView, 42, localOffset);
    centralHeader.set(name, 46);
    centralParts.push(centralHeader);

    localOffset += localHeader.length + content.length;
  });

  const centralSize = centralParts.reduce((total, part) => total + part.length, 0);
  const endRecord = new Uint8Array(22);
  const endView = new DataView(endRecord.buffer);
  write32(endView, 0, 0x06054b50);
  write16(endView, 8, files.length);
  write16(endView, 10, files.length);
  write32(endView, 12, centralSize);
  write32(endView, 16, localOffset);

  return new Blob([...localParts, ...centralParts, endRecord] as any[], {
    type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
  });
};

export const createExcelTable = (
  headers: string[],
  records: Array<Array<string | number>>,
  title: string,
  subtitle: string
) => {
  const safeRecords = records.length
    ? records
    : [["Sin registros para los filtros seleccionados.", ...headers.slice(1).map(() => "")]];
  const lastColumn = columnName(headers.length);
  const lastRow = safeRecords.length + 4;

  const makeCell = (row: number, column: number, value: string | number, style = "") => {
    const reference = `${columnName(column)}${row}`;
    if (typeof value === "number") {
      const styleAttribute = style ? ` s="${style}"` : "";
      return `<c r="${reference}"${styleAttribute}><v>${value}</v></c>`;
    }
    return `<c r="${reference}"${style ? ` s="${style}"` : ""} t="inlineStr"><is><t>${escapeXml(value)}</t></is></c>`;
  };

  const titleRow = `<row r="1">${makeCell(1, 1, title, "1")}</row>`;
  const subtitleRow = `<row r="2">${makeCell(2, 1, subtitle, "2")}</row>`;
  const headerRow = `<row r="4">${headers.map((header, index) => makeCell(4, index + 1, header, "3")).join("")}</row>`;
  const dataRows = safeRecords.map((record, rowIndex) => {
    const rowNumber = rowIndex + 5;
    const cells = headers.map((_, columnIndex) => {
      const value = record[columnIndex] ?? "";
      return makeCell(rowNumber, columnIndex + 1, value, columnIndex === 8 && typeof value === "number" ? "4" : "");
    }).join("");
    return `<row r="${rowNumber}">${cells}</row>`;
  }).join("");

  const columns = headers.map((_, index) => {
    const widths = [13, 30, 20, 27, 34, 28, 12, 12, 13, 16];
    const width = widths[index] || 18;
    return `<col min="${index + 1}" max="${index + 1}" width="${width}" customWidth="1"/>`;
  }).join("");

  const tableColumns = headers.map((header, index) =>
    `<tableColumn id="${index + 1}" name="${escapeXml(header)}"/>`
  ).join("");

  const sheet = `<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <worksheet xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main" xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships">
      <dimension ref="A1:${lastColumn}${lastRow}"/>
      <sheetViews><sheetView workbookViewId="0"><pane ySplit="4" topLeftCell="A5" activePane="bottomLeft" state="frozen"/></sheetView></sheetViews>
      <sheetFormatPr defaultRowHeight="20"/>
      <cols>${columns}</cols>
      <sheetData>${titleRow}${subtitleRow}<row r="3"/>${headerRow}${dataRows}</sheetData>
      <mergeCells count="2"><mergeCell ref="A1:${lastColumn}1"/><mergeCell ref="A2:${lastColumn}2"/></mergeCells>
      <tableParts count="1"><tablePart r:id="rId1"/></tableParts>
    </worksheet>`;

  const table = `<table xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main" id="1" name="ConsolidadoPracticas" displayName="ConsolidadoPracticas" ref="A4:${lastColumn}${lastRow}" totalsRowShown="0">
    <autoFilter ref="A4:${lastColumn}${lastRow}"/>
    <tableColumns count="${headers.length}">${tableColumns}</tableColumns>
    <tableStyleInfo name="TableStyleMedium2" showFirstColumn="0" showLastColumn="0" showRowStripes="1" showColumnStripes="0"/>
  </table>`;

  const styles = `<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <styleSheet xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main">
      <numFmts count="1"><numFmt numFmtId="164" formatCode="0%"/></numFmts>
      <fonts count="4">
        <font><sz val="11"/><name val="Calibri"/></font>
        <font><b/><sz val="16"/><color rgb="FF004B87"/><name val="Calibri"/></font>
        <font><i/><sz val="10"/><color rgb="FF64748B"/><name val="Calibri"/></font>
        <font><b/><color rgb="FFFFFFFF"/><name val="Calibri"/></font>
      </fonts>
      <fills count="3"><fill><patternFill patternType="none"/></fill><fill><patternFill patternType="gray125"/></fill><fill><patternFill patternType="solid"><fgColor rgb="FF004B87"/><bgColor indexed="64"/></patternFill></fill></fills>
      <borders count="2"><border><left/><right/><top/><bottom/><diagonal/></border><border><left style="thin"><color rgb="FFD1D5DB"/></left><right style="thin"><color rgb="FFD1D5DB"/></right><top style="thin"><color rgb="FFD1D5DB"/></top><bottom style="thin"><color rgb="FFD1D5DB"/></bottom><diagonal/></border></borders>
      <cellStyleXfs count="1"><xf numFmtId="0" fontId="0" fillId="0" borderId="0"/></cellStyleXfs>
      <cellXfs count="5">
        <xf numFmtId="0" fontId="0" fillId="0" borderId="0" xfId="0"/>
        <xf numFmtId="0" fontId="1" fillId="0" borderId="0" xfId="0" applyFont="1"/>
        <xf numFmtId="0" fontId="2" fillId="0" borderId="0" xfId="0" applyFont="1"/>
        <xf numFmtId="0" fontId="3" fillId="2" borderId="1" xfId="0" applyFont="1" applyFill="1" applyBorder="1" applyAlignment="1"><alignment horizontal="center" vertical="center" wrapText="1"/></xf>
        <xf numFmtId="164" fontId="0" fillId="0" borderId="0" xfId="0" applyNumberFormat="1"/>
      </cellXfs>
      <cellStyles count="1"><cellStyle name="Normal" xfId="0" builtinId="0"/></cellStyles>
    </styleSheet>`;

  return createZip([
    {
      name: "[Content_Types].xml",
      content: `<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
        <Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">
          <Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml"/>
          <Default Extension="xml" ContentType="application/xml"/>
          <Override PartName="/xl/workbook.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml"/>
          <Override PartName="/xl/worksheets/sheet1.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml"/>
          <Override PartName="/xl/styles.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml"/>
          <Override PartName="/xl/tables/table1.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.table+xml"/>
        </Types>`
    },
    {
      name: "_rels/.rels",
      content: `<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
        <Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
          <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" Target="xl/workbook.xml"/>
        </Relationships>`
    },
    {
      name: "xl/workbook.xml",
      content: `<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
        <workbook xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main" xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships">
          <bookViews><workbookView/></bookViews>
          <sheets><sheet name="Consolidado" sheetId="1" r:id="rId1"/></sheets>
        </workbook>`
    },
    {
      name: "xl/_rels/workbook.xml.rels",
      content: `<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
        <Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
          <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet" Target="worksheets/sheet1.xml"/>
          <Relationship Id="rId2" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles" Target="styles.xml"/>
        </Relationships>`
    },
    { name: "xl/styles.xml", content: styles },
    {
      name: "xl/worksheets/sheet1.xml",
      content: sheet
    },
    {
      name: "xl/worksheets/_rels/sheet1.xml.rels",
      content: `<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
        <Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
          <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/table" Target="../tables/table1.xml"/>
        </Relationships>`
    },
    { name: "xl/tables/table1.xml", content: table }
  ]);
};

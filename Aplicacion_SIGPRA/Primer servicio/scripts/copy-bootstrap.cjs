const { copyFileSync, mkdirSync } = require("node:fs");
const path = require("node:path");

const source = path.dirname(require.resolve("bootstrap/package.json"));
const destination = path.join(__dirname, "..", "vendor", "bootstrap");

for (const [from, to] of [
  ["dist/css/bootstrap.min.css", "css/bootstrap.min.css"],
  ["dist/css/bootstrap.min.css.map", "css/bootstrap.min.css.map"],
  ["dist/js/bootstrap.bundle.min.js", "js/bootstrap.bundle.min.js"],
  ["dist/js/bootstrap.bundle.min.js.map", "js/bootstrap.bundle.min.js.map"],
  ["LICENSE", "LICENSE"],
]) {
  const target = path.join(destination, to);
  mkdirSync(path.dirname(target), { recursive: true });
  copyFileSync(path.join(source, from), target);
}

console.log("Bootstrap: CSS, JavaScript y licencia disponibles en vendor/bootstrap.");

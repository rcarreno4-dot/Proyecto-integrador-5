document.addEventListener('DOMContentLoaded', () => {
  const formPeriod = document.getElementById('form-period');
  const tableBody = document.querySelector('#table-periods tbody');

  // Cargar periodos al iniciar la vista
  loadPeriods();

  formPeriod.addEventListener('submit', async (e) => {
    e.preventDefault();

    // Extraer criterios de la rúbrica y verificar que la suma sea 100%
    const criterioRows = document.querySelectorAll('.criterio-item');
    const criterios = [];
    let sumaPesos = 0;

    criterioRows.forEach(row => {
      const nombre = row.querySelector('.crit-nombre').value;
      const descripcion = row.querySelector('.crit-desc').value;
      const peso = parseFloat(row.querySelector('.crit-peso').value);

      sumaPesos += peso;
      criterios.push({ nombre, descripcion, peso });
    });

    if (sumaPesos !== 100) {
      alert(`Error: La suma de los pesos de la rúbrica es ${sumaPesos}%. Debe sumar exactamente 100%.`);
      return;
    }

    const payload = {
      id_coordinador: 1, // Usuario sesión
      programa: document.getElementById('programa').value,
      nivel_practica: document.getElementById('nivel_practica').value,
      anio: parseInt(document.getElementById('anio').value),
      semestre: parseInt(document.getElementById('semestre').value),
      fecha_inicio: document.getElementById('fecha_inicio').value,
      fecha_fin: document.getElementById('fecha_fin').value,
      fecha_limite_reportes: document.getElementById('fecha_limite_reportes').value,
      horas_minimas: parseInt(document.getElementById('horas_minimas').value),
      criterios
    };

    try {
      const response = await fetch('/api/periods', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      const result = await response.json();

      if (result.success) {
        alert('¡Periodo y rúbrica publicados con éxito!');
        formPeriod.reset();
        loadPeriods();
      } else {
        alert('Error: ' + result.message);
      }
    } catch (error) {
      console.error('Error al enviar formulario:', error);
      alert('Error al conectar con el servidor.');
    }
  });

  async function loadPeriods() {
    try {
      const response = await fetch('/api/periods');
      const result = await response.json();

      if (result.success) {
        tableBody.innerHTML = '';
        result.data.forEach(p => {
          const row = document.createElement('tr');
          row.innerHTML = `
            <td>${p.programa}</td>
            <td>${p.nivel_practica}</td>
            <td>${p.anio}-${p.semestre}</td>
            <td>${p.fecha_inicio.substring(0,10)} a ${p.fecha_fin.substring(0,10)}</td>
            <td>${p.horas_minimas}h</td>
            <td><span class="badge ${p.estado}">${p.estado}</span></td>
          `;
          tableBody.appendChild(row);
        });
      }
    } catch (error) {
      console.error('Error cargando periodos:', error);
    }
  }
});
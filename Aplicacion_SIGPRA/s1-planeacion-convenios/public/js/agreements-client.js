document.addEventListener('DOMContentLoaded', () => {
  const formAgreement = document.getElementById('form-agreement');
  const tableBody = document.querySelector('#table-agreements tbody');

  loadAgreements();

  formAgreement.addEventListener('submit', async (e) => {
    e.preventDefault();

    const payload = {
      nombre_institucion: document.getElementById('nombre_institucion').value,
      nit: document.getElementById('nit').value,
      telefono: document.getElementById('telefono').value,
      correo_contacto: document.getElementById('correo_contacto').value,
      responsable_contacto: document.getElementById('responsable_contacto').value,
      numero_convenio: document.getElementById('numero_convenio').value,
      fecha_inicio: document.getElementById('fecha_inicio_conv').value,
      fecha_fin: document.getElementById('fecha_fin_conv').value,
      nivel_practica: document.getElementById('nivel_practica_plaza').value,
      jornada: document.getElementById('jornada').value,
      cupos_ofrecidos: parseInt(document.getElementById('cupos_ofrecidos').value)
    };

    try {
      const response = await fetch('/api/agreements', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      const result = await response.json();

      if (result.success) {
        alert('Convenio y plaza guardados correctamente.');
        formAgreement.reset();
        loadAgreements();
      } else {
        alert('Error: ' + result.message);
      }
    } catch (error) {
      console.error('Error registrando convenio:', error);
      alert('Error de conexión.');
    }
  });

  async function loadAgreements() {
    try {
      const response = await fetch('/api/agreements');
      const result = await response.json();

      if (result.success) {
        tableBody.innerHTML = '';
        result.data.forEach(item => {
          const row = document.createElement('tr');
          row.innerHTML = `
            <td>${item.institucion_nombre}</td>
            <td>${item.numero_convenio}</td>
            <td>${item.fecha_inicio ? item.fecha_inicio.substring(0,10) : ''} al ${item.fecha_fin ? item.fecha_fin.substring(0,10) : ''}</td>
            <td>${item.jornada || 'N/A'}</td>
            <td>${item.cupos_ocupados || 0} / ${item.cupos_ofrecidos || 0}</td>
            <td><span class="badge ${item.estado_convenio}">${item.estado_convenio}</span></td>
          `;
          tableBody.appendChild(row);
        });
      }
    } catch (error) {
      console.error('Error al cargar convenios:', error);
    }
  }
});
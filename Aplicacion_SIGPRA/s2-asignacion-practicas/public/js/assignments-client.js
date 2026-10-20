document.addEventListener('DOMContentLoaded', () => {
  const formRequest = document.getElementById('form-request');
  const formAssign = document.getElementById('form-assign');
  const outputLog = document.getElementById('output-log');

  // CU-08: Enviar Solicitud de Práctica[cite: 4]
  formRequest.addEventListener('submit', async (e) => {
    e.preventDefault();

    const payload = {
      id_estudiante: parseInt(document.getElementById('req_id_estudiante').value),
      id_periodo: parseInt(document.getElementById('req_id_periodo').value),
      nivel_practica: document.getElementById('req_nivel').value,
      observaciones: document.getElementById('req_observaciones').value
    };

    try {
      const response = await fetch('/api/assignments/request', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      const result = await response.json();
      outputLog.textContent = JSON.stringify(result, null, 2);

      if (result.success) {
        alert('Solicitud de práctica registrada con éxito.');
        formRequest.reset();
      } else {
        alert('Error: ' + result.message);
      }
    } catch (error) {
      console.error('Error al enviar solicitud:', error);
      outputLog.textContent = 'Error de conexión con el microservicio S2.';
    }
  });

  // CU-03: Realizar Asignación (Validando con S1)[cite: 2, 3]
  formAssign.addEventListener('submit', async (e) => {
    e.preventDefault();

    const payload = {
      id_estudiante: parseInt(document.getElementById('assign_id_estudiante').value),
      id_plaza: parseInt(document.getElementById('assign_id_plaza').value),
      id_docente_asesor: parseInt(document.getElementById('assign_id_docente').value),
      id_periodo: parseInt(document.getElementById('assign_id_periodo').value)
    };

    try {
      outputLog.textContent = 'Consultando disponibilidad en Microservicio S1...';

      const response = await fetch('/api/assignments/assign', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      const result = await response.json();
      outputLog.textContent = JSON.stringify(result, null, 2);

      if (result.success) {
        alert('Asignación realizada exitosamente.');
        formAssign.reset();
      } else {
        alert('Respuesta de S2: ' + result.message);
      }
    } catch (error) {
      console.error('Error al realizar asignación:', error);
      outputLog.textContent = 'Error de comunicación inter-servicio o servidor fuera de línea.';
    }
  });
});
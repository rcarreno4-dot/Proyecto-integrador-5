const UserDatabase = [
    { email: 'director@udi.edu.co', pass: 'director123', nombre: 'Dr. Carlos Mendoza', rol: 'Director del Programa', redirect: 'director.html' },
    { email: 'coordinador@udi.edu.co', pass: 'coordinador123', nombre: 'Ing. Martha GÃ³mez', rol: 'Coordinador de PrÃ¡ctica', redirect: 'coordinador.html' },
    { email: 'docente@udi.edu.co', pass: 'docente123', nombre: 'Prof. Roberto Silva', rol: 'Docente Asesor', redirect: 'docente.html' },
    { email: 'estudiante@udi.edu.co', pass: 'estudiante123', nombre: 'Juan Rueda', rol: 'Estudiante', redirect: 'estudiante.html' },
    { email: 'empresa@techsolutions.com', pass: 'empresa123', nombre: 'Tech Solutions S.A.S', rol: 'InstituciÃ³n Receptora', redirect: 'empresa.html' }
];

document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('login-form');
    if (loginForm) {
        loginForm.addEventListener('submit', (e) => {
            e.preventDefault();
            const email = document.getElementById('email').value.trim();
            const password = document.getElementById('password').value.trim();
            const alertBox = document.getElementById('login-alert');

            const foundUser = UserDatabase.find(u => u.email.toLowerCase() === email.toLowerCase() && u.pass === password);

            if (foundUser) {
                localStorage.setItem('sigpra_user', JSON.stringify(foundUser));
                window.location.href = foundUser.redirect;
            } else {
                alertBox.classList.remove('d-none');
                alertBox.textContent = 'Correo electrÃ³nico o contraseÃ±a incorrectos.';
            }
        });
    }
});

function quickFill(email, pass) {
    document.getElementById('email').value = email;
    document.getElementById('password').value = pass;
}
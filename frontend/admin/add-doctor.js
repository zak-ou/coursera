// Load admin name from localStorage
const adminName = localStorage.getItem('adminName') || 'Admin User';
document.getElementById('adminName').textContent = adminName;

// Handle form submission
document.getElementById('addDoctorForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;
    const phone = document.getElementById('phone').value;
    const specialization = document.getElementById('specialization').value;
    const password = document.getElementById('password').value;
    
    // Collect available times
    const availableTimes = {};
    const days = ['monday', 'tuesday', 'wednesday', 'thursday', 'friday'];
    days.forEach(day => {
        const checkbox = document.querySelector(`input[name="${day}"]`);
        const timeInput = checkbox.parentElement.parentElement.querySelector('.time-input');
        if (checkbox.checked && timeInput.value) {
            availableTimes[day.charAt(0).toUpperCase() + day.slice(1)] = [timeInput.value];
        }
    });
    
    const doctorData = {
        name,
        email,
        phone,
        specialization,
        password,
        availableTimes: JSON.stringify(availableTimes)
    };
    
    try {
        const token = localStorage.getItem('adminToken');
        const response = await fetch('http://localhost:8080/api/doctor/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(doctorData)
        });
        
        const data = await response.json();
        
        if (data.success) {
            showMessage('Doctor added successfully!', 'success');
            setTimeout(() => {
                window.location.href = 'doctors-list.html';
            }, 1500);
        } else {
            showMessage(data.message || 'Failed to add doctor', 'error');
        }
    } catch (error) {
        showMessage('Connection error. Please try again.', 'error');
        console.error('Error:', error);
    }
});

function showMessage(text, type) {
    const existingMessage = document.querySelector('.message');
    if (existingMessage) {
        existingMessage.remove();
    }
    
    const message = document.createElement('div');
    message.className = `message ${type}`;
    message.textContent = text;
    
    const form = document.getElementById('addDoctorForm');
    form.parentNode.insertBefore(message, form);
}

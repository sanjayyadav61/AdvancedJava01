/**
 * ShopSphere Frontend JavaScript Client Helper
 */

document.addEventListener('DOMContentLoaded', function () {
    // Auto-dismiss alerts after 5 seconds
    const alerts = document.querySelectorAll('.alert-dismissible');
    alerts.forEach(function (alert) {
        setTimeout(function () {
            const bsAlert = bootstrap.Alert.getOrCreateInstance(alert);
            bsAlert.close();
        }, 5000);
    });

    // Format coupon code inputs to uppercase
    const couponInput = document.getElementById('couponCodeInput');
    if (couponInput) {
        couponInput.addEventListener('input', function () {
            this.value = this.value.toUpperCase().replace(/\s+/g, '');
        });
    }
});

// Quick demo login fill helpers for examiners/students
function fillDemoUser(type) {
    const emailField = document.getElementById('loginEmail');
    const passField = document.getElementById('loginPassword');
    if (!emailField || !passField) return;

    if (type === 'admin') {
        emailField.value = 'admin@shopsphere.com';
        passField.value = 'admin123';
    } else {
        emailField.value = 'customer@shopsphere.com';
        passField.value = 'password123';
    }
}

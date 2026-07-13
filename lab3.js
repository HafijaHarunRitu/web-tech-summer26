function validate(x) {
    
    const errorIds = [
        "fname-error", "lname-error", "gender-error", "father-error", 
        "mother-error", "email-error", "phone-error", "username-error", 
        "password-error", "confirm-error"
    ];
    errorIds.forEach(id => {
        const element = document.getElementById(id);
        if (element) element.innerText = "";
    });

    // 2. Extract input values safely
    const fname = x.fname.value;
    const lname = x.lname.value;
    const father = x.father.value;
    const mother = x.mother.value;
    const email = x.email.value;
    const phone = x.phone.value;
    const username = x.username.value;
    const password = x.password.value;
    const confirm = x.confirm.value;

    let isValid = true;

    // 3. Validation Logic
    if (fname == "") {
        document.getElementById("fname-error").innerText = "First Name is empty";
        isValid = false;
    }

    if (lname == "") {
        document.getElementById("lname-error").innerText = "Last Name is empty";
        isValid = false;
    }

    if (x.gender[0].checked == false && x.gender[1].checked == false) {
        document.getElementById("gender-error").innerText = "Please Select Gender";
        isValid = false;
    }

    if (father == "") {
        document.getElementById("father-error").innerText = "Father's Name is empty";
        isValid = false;
    }

    if (mother == "") {
        document.getElementById("mother-error").innerText = "Mother's Name is empty";
        isValid = false;
    }

    if (email == "") {
        document.getElementById("email-error").innerText = "Email is empty";
        isValid = false;
    }

    if (phone == "") {
        document.getElementById("phone-error").innerText = "Phone Number is empty";
        isValid = false;
    }

    if (username == "") {
        document.getElementById("username-error").innerText = "Username is empty";
        isValid = false;
    }

    if (password == "") {
        document.getElementById("password-error").innerText = "Password is empty";
        isValid = false;
    }

    if (confirm == "") {
        document.getElementById("confirm-error").innerText = "Confirm Password is empty";
        isValid = false;
    } else if (password != confirm) {
        document.getElementById("confirm-error").innerText = "Password and Confirm Password do not match";
        isValid = false;
    }

    
    if (!isValid) {
        return false; 
    }

    alert("Registration Successful");
    return true;
}
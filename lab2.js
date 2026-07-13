function validate(x){

    const fname = x.fname.value;
    const lname = x.lname.value;
    const father = x.father.value;
    const mother = x.mother.value;
    const email = x.email.value;
    const phone = x.phone.value;
    const username = x.username.value;
    const password = x.password.value;
    const confirm = x.confirm.value;

    if(fname==""){
        alert("First Name is empty");
        return false;
    }

    if(lname==""){
        alert("Last Name is empty");
        return false;
    }

    if(x.gender[0].checked==false && x.gender[1].checked==false){
        alert("Please Select Gender");
        return false;
    }

    if(father==""){
        alert("Father's Name is empty");
        return false;
    }

    if(mother==""){
        alert("Mother's Name is empty");
        return false;
    }

    if(email==""){
        alert("Email is empty");
        return false;
    }

    if(phone==""){
        alert("Phone Number is empty");
        return false;
    }

    if(username==""){
        alert("Username is empty");
        return false;
    }

    if(password==""){
        alert("Password is empty");
        return false;
    }

    if(confirm==""){
        alert("Confirm Password is empty");
        return false;
    }

    if(password != confirm){
        alert("Password and Confirm Password do not match");
        return false;
    }

    alert("Registration Successful");
    return true;
}
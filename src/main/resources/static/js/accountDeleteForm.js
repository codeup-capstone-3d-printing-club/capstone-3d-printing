function showAccountDeleteForm(id){
    var formElement = document.getElementById(id);

    if(formElement.style.display === 'none'){
        formElement.style.display = 'block';
    }else
        formElement.style.display = 'none';
}
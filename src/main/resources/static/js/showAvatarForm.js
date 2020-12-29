function showAvatarForm(id){
    var divElement = document.getElementById(id);

    if(divElement.style.display === 'none'){
        divElement.style.display = 'block';
    }else
    divElement.style.display = 'none';
}
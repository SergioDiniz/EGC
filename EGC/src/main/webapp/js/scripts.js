$(document).ready(function () {
    $('.conteudo').animate({opacity: '1.0'})
});

$(document).ready(function () {
    $('.tudo').animate({opacity: '1.0'})
});

$(document).ready(function () {
    $('.test-popup-link').magnificPopup({
        type: 'image'
                // other options
    });

});

$(document).ready(function () {
    $('.popup-with-zoom-anim').magnificPopup({
        type: 'inline',
        fixedContentPos: false,
        fixedBgPos: true,
        overflowY: 'auto',
        closeBtnInside: true,
        preloader: false,
        midClick: true,
        removalDelay: 300,
        mainClass: 'my-mfp-zoom-in'
    });

    $('.popup-with-move-anim').magnificPopup({
        type: 'inline',
        fixedContentPos: false,
        fixedBgPos: true,
        overflowY: 'auto',
        closeBtnInside: true,
        preloader: false,
        midClick: true,
        removalDelay: 300,
        mainClass: 'my-mfp-slide-bottom'
    });



});

$(function () {
    $('.popup-modal').magnificPopup({
        type: 'inline',
        preloader: true,
        focus: '#username',
        modal: true
    });
    $(document).on('click', '.popup-modal-dismiss', function (e) {
        e.preventDefault();
        $.magnificPopup.close();
    });
});


$(document).ready( function(){
    $(".chamar-modal").click( function(){
      $(".remodal").modal("show");
    });  
});
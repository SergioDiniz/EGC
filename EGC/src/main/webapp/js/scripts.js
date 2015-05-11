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

function mostraModalSolicitacao(id) {
    $(document).ready(function () {
        $("#" + id).modal("show");
    })
}


$(function () {
    $('.doc').click(function () {
        var conteudo = $(this).parent().find('.doc-img');
        if (!conteudo.hasClass('show')) {
            $('.caixa').find('.show').slideUp('fast', function () {
                $(this).addClass('hide').removeClass('show')
            });

            conteudo.slideDown('fast', function () {
                $(this).addClass('show').removeClass('hide')
            });
        }
    });
});


function notificacaoSolicicatacao() {
    $.notify({
        title: '<strong>Bem-Vindo!</strong><br/>',
        message: "Exitem novas solicitações a serem visualizadas! <br/> Clique Aqui.",
        url: "solicitacoes.jsf",
        target: "_self",
        delay: 10000
    });
}


$(document).ready(function () {
    var nav = $('.menu-filtro');
    $(window).scroll(function () {
        if ($(this).scrollTop() > 80) {
            nav.addClass("fixar-menu-filtro");
        } else {
            nav.removeClass("fixar-menu-filtro");
        }
    });
});
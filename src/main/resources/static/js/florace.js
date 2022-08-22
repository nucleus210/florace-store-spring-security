$(document).ready(function () {

    // Accordion
    const all_panels = $('.florace-accordion > li > ul').hide();

    $('.florace-accordion > li > a').click(function () {
        console.log('Hello world!');
        const target = $(this).next();
        if (!target.hasClass('active')) {
            all_panels.removeClass('active').slideUp();
            target.addClass('active').slideDown();
        }
        return false;
    });
    // End accordion

    // Product detail
    $('.product-links-wap a').click(function () {
        const this_src = $(this).children('img').attr('src');
        $('#product-detail').attr('src', this_src);
        return false;
    });
    $('#btn-minus').click(function () {
        let val = $("#var-value").html();
        val = (val === '1') ? val : val - 1;
        $("#var-value").html(val);
        $("#product-quanity").val(val);
        return false;
    });
    $('#btn-plus').click(function () {
        let val = $("#var-value").html();
        val++;
        $("#var-value").html(val);
        $("#product-quanity").val(val);
        return false;
    });
    $('.btn-size').click(function () {
        const this_val = $(this).html();
        $("#product-size").val(this_val);
        $(".btn-size").removeClass('btn-secondary');
        $(".btn-size").addClass('btn-success');
        $(this).removeClass('btn-success');
        $(this).addClass('btn-secondary');
        return false;
    });
    // End roduct detail

});

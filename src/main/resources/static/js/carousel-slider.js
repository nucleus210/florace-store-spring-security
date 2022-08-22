$(document).ready(function(){
    // Activate Carousel
    $("#carouselIndicators").carousel();

    // Enable Carousel Indicators
    $(".carouselIndicator01").click(function(){
        $("#carouselIndicators").carousel(0);
    });
    $(".carouselIndicator02").click(function(){
        $("#carouselIndicators").carousel(1);
    });
    $(".carouselIndicator03").click(function(){
        $("#carouselIndicators").carousel(2);
    });
    // Enable Carousel Controls
    $(".carousel-control-prev").click(function(){
        $("#carouselIndicators").carousel("prev");
    });
    $(".carousel-control-next").click(function(){
        $("#carouselIndicators").carousel("next");
    });
});
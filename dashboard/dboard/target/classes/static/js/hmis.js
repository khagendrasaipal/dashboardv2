/*!
 =========================================================
 * Khabar64 Custom Javascripts - v1.0.0
 =========================================================
 * Product Page: https://www.creative-tim.com/product/paper-kit
 * Copyright 2022 Suman Shrestha (https://www.suman-shrestha.com.np)

 * Coded by Suman Shrestha suman.shrestha2009@gmail.com
 =========================================================
 */


jQuery( document ).ready( function( $ ) {

    //Tab to top
    $(window).scroll(function() {
        if ($(this).scrollTop() > 1){  
            $('.scroll-top-wrapper').addClass("show");
        }
        else{
            $('.scroll-top-wrapper').removeClass("show");
        }
    });

    $(".scroll-top-wrapper").on("click", function() {
        $("html, body").animate({ scrollTop: 0 }, 600);
        return false;
    });





    //owl-carousel team-list
    $('.team-listing').owlCarousel({
        autoplay:true,
        autoplayTimeout:4000,
        smartSpeed:2000,
        loop:true,
        dots:false,
        nav:false,
        navText: ['<i class="fa fa-angle-left fa-4x"></i>','<i class="fa fa-angle-right fa-4x"></i>'], 
        margin:20,
        items:4,
        singleItem:true,
        responsiveClass:true,
        responsive:{
            0:{
                items:1,
                nav:false
            },
            600:{
                items:1,
                nav:false
            },
            900:{
                items:2,
                nav:false,
                loop:true
            },
            1280:{
                items:3,
                nav:false,
                loop:true
            }
        }
    });


});
var $ = function (i) { return document.querySelector(i); };
var $$ = function (i) { return document.querySelectorAll(i); };

var bannerMain = new Swiper ('#banner-main', {
  // Optional parameters
  direction: 'vertical',
  loop: true,
  // Navigation arrows
  // nextButton: '#swiper-button-next',
  // prevButton: '#swiper-button-prev',
  speed: 500,
  autoplay: 3000,
  autoplayDisableOnInteraction: false,
});
var bannerOther = new Swiper ('#banner-other', {
  // Optional parameters
  direction: 'vertical',
  loop: true,
  // Navigation arrows
  // nextButton: '#swiper-button-next',
  // prevButton: '#swiper-button-prev',
  speed: 500,
  autoplay: 3000,
  autoplayDisableOnInteraction: false,
});


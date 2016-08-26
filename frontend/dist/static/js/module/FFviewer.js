VueTouch.registerCustomEvent('doubletap', {
  type: 'tap',
  taps: 2
});

Vue.component("ffviewer", {
  template: '<div id="FFviewer" style="user-select: none" v-if="status">' +
    '<div id="FFviewer-close-btn" @click.prevent="closeViewer"></div>' +
    '<div id="FFviewer-img-wrap" v-if="imgsrc" @mousewheel.prevent="zoomViewerByWheel" v-touch:panstart="dragViewerStart" v-touch:pan="dragViewer" v-touch:doubletap="doubletapViewer" v-touch:pinch="zoomViewer" v-touch:pinchend="zoomViewerEnd">' +
      '<img v-el:ffviewer-img id="FFviewer-img" :style="imgStyle" :src="imgsrc" alt="">' +
    '</div>' +
  '</div>',
  props: {
    status: {
      type: Boolean,
      required: true
    },
    imgsrc: "",
    imglist: []
  },
  data: function(){
    return {
      imgWidth: 0,
      imgHeight: 0,
      windowWidth: 0,
      windowHeight: 0,
      PADDING: 40,
      resetStatus: false,
      defaultRatio: 1,
      ratio: 1,
      x: 0,
      y: 0,
      startX: 0,
      startY: 0,
      endRatio: 1,
    }
  },
  computed: {
    imgStyle: function(){
      return {
        "touch-action": "none",
        "-webkit-user-select": "none",
        "-webkit-user-drag": "none",
        "-webkit-tap-highlight-color": "rgba(0, 0, 0, 0)",
        transform: "scale(" + this.ratio + ")",
        top: this.y + 'px',
        left: this.x + 'px',
      }
    },
  },
  watch: {
    "status": function(){
      this.reset();
    },
    "imgsrc": function(){
      this.reset();
    },
    "ratio": function(value){
      if(value < 0.25){
        this.ratio = 0.25;
      }else if(value > 5){
        this.ratio = 5;
      }
    }
  },
  methods: {
    reset: function(){
      var img = this.$els.ffviewerImg;
      if(this.status && img){
        this.imgWidth = img.width;
        this.imgHeight = img.height;
        this.windowWidth = window.innerWidth;
        this.windowHeight = window.innerHeight;
        this.endRatio = this.defaultRatio = this.ratio = Math.min((this.windowWidth-this.PADDING) / this.imgWidth, (this.windowHeight-this.PADDING) / this.imgHeight, 1);
        this.$set('x', this.windowWidth/2 - this.imgWidth/ 2);
        this.$set('y', this.windowHeight/2 - this.imgHeight/ 2);
        this.resetStatus = true;
      }

    },
    dragViewerStart: function(event){
      this.resetStatus = false;
      this.$set('startX', this.x);
      this.$set('startY', this.y);
    },
    dragViewer: function(event){
      var newX = this.startX + event.deltaX;
      var newY = this.startY + event.deltaY;
      if(newX > -0.5*(this.imgWidth*(1+this.ratio))+this.PADDING && newX < this.windowWidth-0.5*(this.imgWidth*(1-this.ratio))-this.PADDING){
        this.$set('x', newX);
      }
      if(newY > -0.5*(this.imgHeight*(1+this.ratio))+this.PADDING && newY < this.windowHeight-0.5*(this.imgHeight*(1-this.ratio))-this.PADDING){
        this.$set('y', newY);
      }
    },
    zoomViewerByWheel: function(event){
      var direct =0;
      if (event.wheelDelta) {
        direct = event.wheelDelta;
      } else {
        direct = -event.detail * 20;
      }
      if(direct < 0) {
        this.zoomTo(0.9);
      }else if(direct > 0) {
        this.zoomTo(1.1);
      }
    },
    zoomViewer: function(event){
      this.resetStatus = false;
      var newRatio = this.endRatio * event.scale;
      this.$set('ratio', newRatio);
    },
    zoomViewerEnd: function(event){
      this.$set('endRatio', this.ratio);
    },
    zoomTo: function(scale){
      this.resetStatus = false;
      this.endRatio = this.ratio = this.ratio*scale;
    },
    doubletapViewer: function(event){
      if(!this.resetStatus){
        this.reset();
      }else{
        this.zoomTo(2);
      }
    },
    closeViewer: function(){
      this.status = false;
    },
  },
  ready: function(){
    var _this = this;
    window.onresize = function(){
      _this.reset();
    }
  }
});


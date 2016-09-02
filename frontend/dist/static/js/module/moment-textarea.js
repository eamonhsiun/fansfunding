Vue.component('moment-textarea', {
  template: `
  <div class="comment-text comment-wrap">
    <div class="comment-userinfo">
      <div class="comment-avatar">
        <img v-bind:src="userInfo.head | resource" alt="">
      </div>
      <div class="comment-userinfo-name">{{ userInfo.nickname }}</div>
      <!-- <div class="comment-unerinfo-intro">口发起头衔是什么好口怕发怕么好口怕</div> -->
    </div>
    <div class="comment-input">
      <textarea name="" v-model="content"></textarea >
      <div class="comment-input-more">
        <div class="comment-hint" v-text="hint" v-bind:class="{'overflow':overflow}"></div>
        <div class="comment-submit" v-on:click="submit" v-text="btnText"></div>
      </div>
    </div>
  </div>
  `,
  props: {
    userInfo: {
      type: Object,
      required: true
    },
    btnText: {
      type: String,
      default: "发表动态"
    },
    maxCount: {
      type: Number,
      default: 128
    }
  },
  data: function(){
    return {
      content: "",
      images: [],
    }
  },
  computed: {
    count: function(){
      return this.content.length;
    },
    difference: function(){
      return this.maxCount - this.count;
    },
    hint: function(){
      if(this.overflow){
        return "超出" + -this.difference + "个字";
      }else{
        return "剩余" + this.difference + "个字";
      }
    },
    overflow: function(){
      if(this.difference >= 0){
        return false;
      }else{
        return true;
      }
    }
  },
  methods: {
    submit: function(){
      if(!this.count || this.overflow){
        return;
      }
      this.$emit("onsubmit", {content: this.content, images: this.images});
    }
  }
});

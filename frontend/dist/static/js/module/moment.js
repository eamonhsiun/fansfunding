Vue.component('moment', {
  template:`
  <div class="kit-content">
    <comment-textarea :user-info="userInfo" v-if="isSelf && showInput" @onsubmit="sendMoment"></comment-textarea>
    <ffloader></ffloader>
    <ul>
      <li class="comment-wrap" v-for="moment in list" track-by="$index">
        <div class="comment-userinfo">
          <div class="comment-avatar">
            <img v-bind:src="moment.user.head | resource" alt="">
          </div>
          <a href="space.html?userid={{moment.user.id}}"><div class="comment-userinfo-name">{{ moment.user.nickname }}</div></a>
          <!-- <div class="comment-userinfo-intro">{{ moment.user.intro }}</div> -->
        </div>
        <div class="comment-content">
          <div class="comment-inner">
            <div>{{ moment.content }}</div>
            <div class="comment-images" v-if="moment.images.length > 0">
              <div class="comment-image" v-for="image in moment.images" track-by="$index">
                <img class="zoom" v-bind:src="image | resource" @click.prevent="viewPic" alt="">
              </div>
            </div>
          </div>
          <div class="comment-more">
            <div class="comment-time">{{ moment.postTime | commentTime }}</div>
            <div class="comment-btns">
              <div v-if="showLike" :class="{'liked': moment.isLike}" class="comment-btn comment-like" @click.prevent="likeMoment(moment)">(<span v-text="moment.likeNum"></span>)</div>
              <div v-if="showComment" class="comment-btn comment-reply" v-on:click.prevent="showComments(moment.momentId)">评论(<span v-text="moment.commentNum"></span>)</div>
              <div v-if="showForward" class="comment-btn comment-reply" v-on:click.prevent="replyComment(comment.commenterId, comment.commenterNickname)">转发(<span v-text="moment.forwardNum"></span>)</div>
            </div>
          </div>
          <inner-comment :moment="moment" :user-info="userInfo"></inner-comment>
        </div>
      </li>
    </ul>
    <pagination :pagination="pagination" :callback="momentFilter" :offset="3"></pagination>
  </div>
  `,
  props: {
    isSelf: {
      type: Boolean,
      required: true
    },//是否是自己
    userInfo: {
      type: Object,
      required: true
    },//自己的信息
    targetUserInfo: {
      type: Object,
    },//目标用户的信息
    momentType: {
      type: String,//'user', 'follower', 'project'
      default: 'user'
    },
    showInput: {
      type: Boolean,
      default: true
    },
    showLike: {
      type: Boolean,
      default: false,
    },
    showForward: {
      type: Boolean,
      default: false
    },
    showComment: {
      type: Boolean,
      default: false
    },
  },
  data: function(){
    return {
      list: [],
      pagination: {
        total: 0,
        pageSize: 0,
        pages: 0,
        pageNum: 0,
      },
    };
  },
  watch: {

  },
  computed: {
    momentUserInfo: function(){
      if(this.targetUserInfo){
        return this.targetUserInfo;
      }
      return this.userInfo;
    }
  },
  methods: {
    viewPic: function(event){
      this.$dispatch('moment-img-show', event.target.src);
    },
    momentFilter: function(page){
      var url = null;
      switch (this.momentType){
        case 'user':
          url = apiUrl +"/user/" + this.momentUserInfo.id + "/moment";
          break;
        case 'follower':
          url = apiUrl +"/user/" + this.userInfo.id + "/moment/follow";
          break;
      }
      this.getMoments(url, page);
      this.$broadcast('inner-comment-close');
    },
    getMoments: function(url, page){
      if(!url){
        return;
      }
      var _this = this;
      var momentsRequest = ajax({
        method: 'get',
        url: url + "?token=" + localToken + '&rows=12' + (page ? "&page=" + page : ""),
      }).then(function (response, xhr) {
        if(!response.result){
          _this.$broadcast('ffloader-failure', "获取动态失败");
        }else{
          _this.list = response.data.list;
          if(_this.list.length === 0){
            _this.$broadcast('ffloader-failure', "没有动态");
          }else{
            _this.$broadcast('ffloader-success');
          }
          _this.setPagination(_this.pagination, response.data);
        }
      }).catch(function (response, xhr) {
        _this.$broadcast('ffloader-error');
      }).always(function (response, xhr) {
        // Do something
      });
    },
    sendMoment: function(data){
      var _this = this;
      _this.$broadcast('ffloader-init');
      var momentRequest = ajax({
        method: 'post',
        url: apiUrl +"/user/" + localId + "/moment",
        data: {
          token: localToken,
          content: data.content,
          images: data.images,
        }
      }).then(function (response, xhr) {
        if(!response.result){
          _this.$broadcast('ffloader-failure', "发送失败");
        }else{
          _this.momentFilter();
        }
      }).catch(function (response, xhr) {
        _this.$broadcast('ffloader-error');
      }).always(function (response, xhr) {
        // Do something
      });
    },
    likeMoment: function(moment){
      var _this = this;
      var url = "";
      if(moment.isLike){
        url = apiUrl +"/user/" + moment.user.id + "/moment/" + moment.momentId + "/unlike";
      }else{
        url = apiUrl +"/user/" + moment.user.id + "/moment/" + moment.momentId + "/like";
      }
      ajax({
        method: 'post',
        url: url,
        data: {
          token: localToken,
        }
      }).then(function (response, xhr) {
        if(!response.result){
          console.log('失败')
        }else{
          if(moment.isLike){
            moment.likeNum -= 1;
          }else{
            moment.likeNum += 1
          }
          moment.isLike = !moment.isLike;
        }
      }).catch(function (response, xhr) {
        // _this.$broadcast('ffloader-error');
      }).always(function (response, xhr) {
        // Do something
      });
    },
    showComments: function(momentId){
      this.$broadcast('inner-comment-toggle', momentId);
    },
  },
});

Vue.component('comment-textarea', {
  template: `
  <div class="comment-text comment-wrap">
    <div class="comment-userinfo" v-if="userInfo">
      <div class="comment-avatar">
        <img v-bind:src="userInfo.head | resource" alt="">
      </div>
      <div class="comment-userinfo-name">{{ userInfo.nickname }}</div>
      <!-- <div class="comment-unerinfo-intro">口发起头衔是什么好口怕发怕么好口怕</div> -->
    </div>
    <div class="comment-input">
      <textarea v-el:textarea v-model="content" :placeholder="placeholder"></textarea >
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
    },
    btnText: {
      type: String,
      default: "发表动态"
    },
    placeholder: {
      type: String,
      default: ""
    },
    maxCount: {
      type: Number,
      default: 128
    },
  },
  data: function(){
    return {
      content: "",
      images: "",
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
      this.content = "";
    },
  },
});

Vue.component("inner-comment", {
  template:`
  <div class="inner-comment" v-if="show">
    <comment-textarea btn-text="评论" @onsubmit="sendInnerComment"></comment-textarea>
    <ffloader></ffloader>
    <ul>
      <li class="comment-wrap" v-for="comment in list" track-by="$index">
        <div class="comment-userinfo">
          <div class="comment-avatar">
            <img v-bind:src="comment.user.head | resource" alt="">
          </div>
          <a href="space.html?userid={{comment.user.id}}"><div class="comment-userinfo-name">{{ comment.user.nickname }}</div></a></div>
        <div class="comment-content">
          <div class="comment-inner">
            <div>{{ (comment.replyTo ? "回复" + comment.replyTo.nickname + "：" : "") + comment.content}}</div>
          </div>
          <div class="comment-more">
            <div class="comment-time">{{ comment.postTime | commentTime }}</div>
            <div class="comment-btns">
              <div class="comment-btn comment-reply" v-on:click.prevent="showReplyInput(comment.commentId)">回复</div>
            </div>
          </div>
          <reply-comment :user="comment.user" :comment-id="comment.commentId" @onreply="sendInnerComment"></reply-comment>
        </div>
      </li>
    </ul>
    <pagination :pagination="pagination" :callback="getComments" :offset="3"></pagination>
  </div>
  `,
  props: {
    moment: {
      type: Object,
      required: true
    },
    userInfo: {
      type: Object,
      required: true
    },
  },
  data: function(){
    return {
      show: false,
      list: [],
      pagination: {
        total: 0,
        pageSize: 0,
        pages: 0,
        pageNum: 0,
      },
    };
  },
  events: {
    'inner-comment-toggle': function(momentId){
      if(momentId == this.moment.momentId){
        if(this.show){
          this.show = false;
        }else{
          this.show = true;
          this.getComments();
        }
      }
    },
    'inner-comment-close': function(){
      this.show = false;
      return true;
    }
  },
  methods: {
    getComments: function(page){
      var _this = this;
      var commentsRequest = ajax({
        method: 'get',
        url: apiUrl +"/user/moment/" + _this.moment.momentId + "/comment?token=" + localToken + '&rows=12' + (page ? "&page=" + page : ""),
      }).then(function (response, xhr) {
        if(!response.result){
          _this.$broadcast('ffloader-failure', "获取评论失败");
        }else{
          _this.list = response.data;
          if(_this.list.length === 0){
            _this.$broadcast('ffloader-failure', "没有相关评论");
          }else{
            _this.$broadcast('ffloader-success');
          }
          _this.moment.commentNum = _this.list.length;
          // _this.setPagination(_this.pagination, response.data);
        }
      }).catch(function (response, xhr) {
        _this.$broadcast('ffloader-error');
      }).always(function (response, xhr) {
        _this.$broadcast('reply-comment-close');
      });
    },
    sendInnerComment: function(data){
      var _this = this;
      _this.$broadcast('ffloader-init');
      var momentRequest = ajax({
        method: 'post',
        url: apiUrl +"/user/" + localId + "/moment/" + _this.moment.momentId + "/comment",
        data: {
          token: localToken,
          content: data.content,
          replyTo: data.replyTo !== null ? data.replyTo : 0,
        }
      }).then(function (response, xhr) {
        if(!response.result){
          _this.$broadcast('ffloader-failure', "评论失败");
        }else{
          _this.getComments();
        }
      }).catch(function (response, xhr) {
        _this.$broadcast('ffloader-error');
      }).always(function (response, xhr) {
        // Do something
      });
    },
    showReplyInput: function(commentId){
      this.$broadcast('reply-comment-toggle', commentId);
    },
  },
  components: {
    'reply-comment': {
      template:`
        <div class="reply-comment" v-if="show">
          <comment-textarea btn-text="回复" :placeholder="placeholder" @onsubmit="reply"></comment-textarea>
        </div>
      `,
      props: {
        user: {
          type: Object,
          required: true,
        },
        commentId: {
          type: Number,
          required: true,
        }
      },
      computed: {
        placeholder: function(){
          if(this.user.nickname){
            return "回复" + this.user.nickname + "：";
          }
          return "";
        }
      },
      data: function(){
        return {
          show: false,
        };
      },
      events: {
        'reply-comment-toggle': function(commentId){
          if(commentId == this.commentId){
            if(this.show){
              this.show = false;
            }else{
              this.show = true;
            }
          }
        },
        'reply-comment-close': function(){
          this.show = false;
          return true;
        }
      },
      methods: {
        reply: function(data){
          data.replyTo = this.user.id;
          this.$emit("onreply", data);
        }
      }
    }
  }
});

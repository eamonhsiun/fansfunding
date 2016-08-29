Vue.component('moment', {
  template:`
  <div class="kit-content">
    <moment-textarea :user-info="userInfo" v-if="isSelf && showInput" @onsubmit="sendMoment"></moment-textarea>
    <ffloader></ffloader>
    <ul>
      <li class="moment-wrap comment-wrap" v-for="moment in list" track-by="$index">
        <div class="moment-userinfo comment-userinfo">
          <div class="moment-avatar comment-avatar">
            <img v-bind:src="moment.user.head | resource" alt="">
          </div>
          <a href="space.html?userid={{moment.user.id}}"><div class="moment-userinfo-name comment-userinfo-name">{{ moment.user.nickname }}</div></a>
          <!-- <div class="moment-userinfo-intro comment-userinfo-intro">{{ moment.user.intro }}</div> -->
        </div>
        <div class="moment-content comment-content">
          <div class="moment-inner comment-inner">
            <div>{{ moment.content }}</div>
            <div class="moment-images comment-images">
              <div class="comment-image" v-for="image in moment.images" track-by="$index">
                <img class="zoom" v-bind:src="image | resource" @click.prevent="viewPic" alt="">
              </div>
            </div>
          </div>
          <div class="moment-more comment-more">
            <div class="moment-time comment-time">{{ moment.postTime | commentTime }}</div>
            <div class="moment-btns comment-btns">
              <div v-if="showLike" class="moment-btn moment-like comment-btn comment-like" @click.prevent="likeMoment(moment.momentId)">(<span v-text="moment.likeNum"></span>)</div>
              <div v-if="showComment" class="comment-btn comment-reply" v-on:click.prevent="showComments(moment.momentId)">评论(<span v-text="moment.commentNum"></span>)</div>
              <div v-if="showForward" class="comment-btn comment-reply" v-on:click.prevent="replyComment(comment.commenterId, comment.commenterNickname)">转发(<span v-text="moment.forwardNum"></span>)</div>
            </div>
          </div>
          <inner-comment :moment-id="moment.momentId" :user-info="userInfo"></inner-comment>
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
    setPagination: function(pagination, data){
      pagination.total = data.total;
      pagination.pageNum = data.pageNum;
      pagination.pages = data.pages;
      pagination.pageSize = data. pageSize;
    },
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
          url = apiUrl +"/user/" + this.momentUserInfo.id + "/moment/follow";
          break;
      }
      this.getMoments(url, page);
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
          _this.input.content = "";
        }
      }).catch(function (response, xhr) {
        _this.$broadcast('ffloader-error');
      }).always(function (response, xhr) {
        // Do something
      });
    },
    likeMoment: function(momentId){
      var _this = this;
      ajax({
        method: 'post',
        url: apiUrl +"/user/" + localId + "/moment/" + momentId + "/like",
        data: {
          // userId: localId,
          token: localToken,
        }
      }).then(function (response, xhr) {
        console.log(response.data)
        console.log(response.errCode)
        if(!response.result){
          // _this.$broadcast('ffloader-failure', "发送失败");
          console.log('点赞失败')
        }else{
          // _this.momentFilter();
          // _this.input.content = "";
          console.log('点赞成功')
        }
      }).catch(function (response, xhr) {
        // _this.$broadcast('ffloader-error');
      }).always(function (response, xhr) {
        // Do something
      });
    },
    showComments: function(momentId){
      this.$broadcast('inner-comment-toggle', momentId);
    }
  },
});

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
    }
  },
  events: {

  }
});

Vue.component("inner-comment", {
  template:`
  <div class="inner-comment" v-if="show">
    <moment-textarea :user-info="userInfo" @onsubmit=""></moment-textarea>
    <ffloader></ffloader>
    <ul>
      <li class="moment-wrap comment-wrap" v-for="comment in list" track-by="$index">
        <div class="moment-userinfo comment-userinfo">
          <div class="moment-avatar comment-avatar">
            <img v-bind:src="comment.user.head | resource" alt="">
          </div>
          <a href="space.html?userid={{comment.user.id}}"><div class="moment-userinfo-name comment-userinfo-name">{{ comment.user.nickname }}</div></a></div>
        <div class="moment-content comment-content">
          <div class="moment-inner comment-inner">
            <div>{{ comment.content }}</div>
          </div>
        </div>
      </li>
    </ul>
    <pagination :pagination="pagination" :callback="getComments" :offset="3"></pagination>
  </div>
  `,
  props: {
    momentId: {
      type: Number,
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
      if(momentId == this.momentId){
        if(this.show){
          this.show = false;
        }else{
          this.show = true;
          this.getComments();
        }
      }
    }
  },
  methods: {
    getComments: function(page){
      var _this = this;
      var commentsRequest = ajax({
        method: 'get',
        url: apiUrl +"/user/moment/" + _this.momentId + "/comment?token=" + localToken + '&rows=12' + (page ? "&page=" + page : ""),
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
          // _this.setPagination(_this.pagination, response.data);
        }
      }).catch(function (response, xhr) {
        _this.$broadcast('ffloader-error');
      }).always(function (response, xhr) {
        // Do something
      });
    },
  }


});

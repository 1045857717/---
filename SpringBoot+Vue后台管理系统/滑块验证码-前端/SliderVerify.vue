<!--
vue2滑动验证组件
依赖 element-ui、ajxos
-->
<template>
  <div class="slider" v-show="show">
    <div class="content">
      <div class="bg-img-div">
        <img id="bg-img" :src="data.bgImage" alt/>
      </div>
      <div class="slider-img-div" :style="sliderImgStyles">
        <img id="slider-img" :src="data.sliderImage" alt/>
      </div>
    </div>
    <div class="slider-move">
      <div class="slider-move-track">
        拖动滑块完成拼图
      </div>
      <div class="slider-move-btn" :style="moveStyles"
           @mousedown="$_slider_mousedown($event)"
           @touchstart="$_slider_touchstart($event)"></div>
    </div>
    <div class="bottom">
      <div class="close-btn" v-show="showClose" @click="$_close"></div>
      <div class="refresh-btn" @click="$_refresh"></div>
    </div>
  </div>
</template>

<script>

export default {
  name: "SliderVerify",
  props: {
    defaultShow: {type: Boolean, default: false},
    defaultGenUrl: {type: String, default: '/slider_code'},
    defaultCheckUrl: {type: String, default: '/slider_check'},
    showClose: {type: Boolean, default: false}
  },
  data() {
    return {
      data: {
        bgImage: '',
        sliderImage: '',
        id: ''
      },
      genUrl: this.defaultGenUrl,
      checkUrl: this.defaultCheckUrl,
      checkData: {
        id: '',
        track: ''
      },
      start: 0,
      startY: 0,
      end: 206,
      movePercent: 0,
      // 滑动轨迹滑动时间等数据
      trackData: {
        bgImageWidth: 0,
        bgImageHeight: 0,
        sliderImageWidth: 0,
        sliderImageHeight: 0,
        startSlidingTime: null,
        entSlidingTime: null,
        trackList: []
      },
      moveStyles: {
        transform: 'translate(0px, 0px)',
        backgroundPosition: '-5px 11.79625%'
      },
      sliderImgStyles: {
        transform: 'translate(0px, 0px)',
      },
      show: this.defaultShow
    }
  },
  created() {
    this.$_gen()
  },
  mounted() {
    const bgElements = document.getElementsByClassName('bg-img-div')
    this.trackData.bgImageWidth = bgElements.item(0).clientWidth
    this.trackData.bgImageHeight = bgElements.item(0).clientHeight
    const sliderElements = document.getElementsByClassName('slider-img-div')
    this.trackData.sliderImageWidth = sliderElements.item(0).clientWidth
    this.trackData.sliderImageHeight = sliderElements.item(0).clientHeight
  },
  methods: {
    /**
     * 生成滑动验证图片
     */
    $_gen() {
      this.$_reset()
      this.$http.get(this.genUrl).then(res => {
        this.data = res.data.captcha
        this.checkData.id = this.data.id
      })
    },
    /**
     * 验证
     */
    $_check() {
      this.checkData.track = JSON.stringify(this.trackData)
      this.$http.get(this.checkUrl, {params: this.checkData}).then(res => {
        const result = res.data.result
        this.$emit('check', result)
        if (!result) {
          this.$message.error('验证失败')
          this.$_refresh()
        }
      }).catch(err => this.$_refresh())

    },
    /**
     * 刷新
     */
    $_refresh() {
      this.$_gen()
    },
    /**
     * 重置
     */
    $_reset() {
      this.moveStyles.transform = 'translate(0, 0)'
      this.moveStyles.backgroundPosition = '-5px 11.79625%'
      this.sliderImgStyles.transform = 'translate(0, 0)'
      this.start = 0
      this.startY = 0
      this.movePercent = 0
      this.checkData.track = null
      this.checkData.id = null
      this.data.id = null
      this.trackData.startSlidingTime = null
      this.trackData.entSlidingTime = null
      this.trackData.trackList = []
    },
    $_close() {
      this.show = false
    },

    /*
     * 滑动图片鼠标按下
     */
    $_slider_mousedown(event) {
      this.trackData.startSlidingTime = new Date()
      this.start = event.pageX
      this.startY = event.pageY
      this.moveStyles.backgroundPosition = '-5px 31.0092%'
      window.addEventListener("mousemove", this.$_move)
      window.addEventListener("mouseup", this.$_up)
    },
    /*
     * 滑动图片触摸按下
     */
    $_slider_touchstart(event) {
      this.trackData.startSlidingTime = new Date()
      this.start = event.pageX
      this.startY = event.pageY
      if (this.start === undefined) {
        this.start = event.originalEvent.targetTouches[0].pageX
        this.startY = event.originalEvent.targetTouches[0].pageY;
      }
      this.moveStyles.backgroundPosition = '-5px 31.0092%'
      window.addEventListener("mousemove", this.$_move)
      window.addEventListener("mouseup", this.$_up)
    },
    /**
     * 移动
     * @param event
     */
    $_move(event) {
      if (event instanceof TouchEvent) {
        event = event.touches[0]
      }
      let moveX = event.pageX - this.start
      let pageX = event.pageX
      let pageY = event.pageY
      this.trackData.trackList.push({
        x: pageX - this.start,
        y: pageY - this.startY,
        t: (new Date().getTime() - this.trackData.startSlidingTime.getTime())
      })
      if (moveX < 0) {
        moveX = 0
      } else if (moveX > this.end) {
        moveX = this.end
      }
      // if (moveX > 0 && moveX <= end) {
      this.moveStyles.transform = 'translate(' + moveX + 'px, 0px)'
      this.sliderImgStyles.transform = 'translate(' + moveX + 'px, 0px)'
      // }
      this.movePercent = moveX / this.trackData.bgImageWidth
    },
    /**
     * 鼠标松下,进行验证
     * @param event
     */
    $_up(event) {
      this.trackData.entSlidingTime = new Date()
      window.removeEventListener("mousemove", this.$_move)
      window.removeEventListener("mouseup", this.$_up)
      this.checkData.track = JSON.stringify(this.trackData)
      this.$emit('move', this.checkData)
      // this.$_check()
    }
  }
}
</script>

<style scoped>
.slider {
  background-color: #fff;
  width: 278px;
  height: 285px;
  z-index: 999;
  box-sizing: border-box;
  padding: 9px;
  border-radius: 6px;
  box-shadow: 0 0 11px 0 #999999;
}

.slider .content {
  width: 100%;
  height: 159px;
  position: relative;
}

.bg-img-div,
.slider-img-div {
  width: 100%;
  height: 100%;
  position: absolute;
  transform: translate(0px, 0px);
}

.bg-img-div img {
  width: 100%;
}

.slider-img-div img {
  height: 100%;
}

.slider .slider-move {
  height: 60px;
  width: 100%;
  margin: 11px 0;
  position: relative;
}

.slider .bottom {
  height: 19px;
  width: 100%;
}

.refresh-btn, .close-btn, .slider-move-track, .slider-move-btn {
  background: url(https://static.geetest.com/static/ant/sprite.1.2.4.png) no-repeat;
}

.refresh-btn, .close-btn {
  display: inline-block;
}

.slider-move .slider-move-track {
  line-height: 38px;
  font-size: 14px;
  text-align: center;
  white-space: nowrap;
  color: #88949d;
  -moz-user-select: none;
  -webkit-user-select: none;
  user-select: none;
}

.slider {
  user-select: none;
}

.slider-move .slider-move-btn {
  transform: translate(0px, 0px);
  background-position: -5px 11.79625%;
  position: absolute;
  top: -12px;
  left: 0;
  width: 66px;
  height: 66px;
}

.slider-move-btn:hover, .close-btn:hover, .refresh-btn:hover {
  cursor: pointer
}

.bottom .close-btn {
  width: 20px;
  height: 20px;
  background-position: 0 44.86874%;
}

.bottom .refresh-btn {
  width: 20px;
  height: 20px;
  background-position: 0 81.38425%;
  margin-left: 10px;
}
</style>

<template>
  <div class="slider">
    <div class="content">
      <div class="bg-img-div" ref="bgImg">
        <img id="bg-img" :src="backgroundImage" alt/>
      </div>
      <div class="slider-img-div" ref="sliderImg">
        <img id="slider-img" :src="sliderImage" alt/>
      </div>
    </div>
    <div class="slider-move">
      <div class="slider-move-track">
        拖动滑块完成拼图
      </div>
      <div class="slider-move-btn" ref="sliderBtn"
      />
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name:'sliderCaptcha',
  data() {
    return {
      currentCaptchaId: '',
      backgroundImage: '',
      sliderImage: '',
      isMouseDown: false,
      originX: 0,
      originY: 0,
      startSlidingTime: null,
      entSlidingTime: null,
      trackList: [],
      bgImageWidth: 0,
      bgImageHeight: 0,
      sliderImageWidth: 0,
      sliderImageHeight: 0,
    }
  },
  methods: {
    refreshCaptcha() {
      this.currentCaptchaId= ''
      this.backgroundImage= ''
      this.sliderImage= ''
      this.isMouseDown= false
      this.originX= 0
      this.originY= 0
      this.startSlidingTime= null
      this.entSlidingTime= null
      this.trackList= []
      this.bgImageWidth= 0
      this.bgImageHeight= 0
      this.sliderImageWidth= 0
      this.sliderImageHeight= 0
      request("/gen").then(res => {
        let data = res.data;
        this.currentCaptchaId = data.id;
        this.backgroundImage = data.captcha.backgroundImage;
        this.sliderImage = data.captcha.sliderImage
        this.$refs.sliderImg.style.transform = 'translate(0px, 0px)';
        this.$refs.sliderBtn.style.transform = 'translate(0px, 0px)';
      })
    },
    valid() {
      let data = {
        bgImageWidth: this.$refs.bgImg.clientWidth,
        bgImageHeight: this.$refs.bgImg.clientHeight,
        sliderImageWidth: this.$refs.sliderImg.clientWidth,
        sliderImageHeight: this.$refs.sliderImg.clientHeight,
        startSlidingTime: this.startSlidingTime,
        entSlidingTime: this.entSlidingTime,
        trackList: this.trackList,
      };
      request.post("/check?id=" + this.currentCaptchaId,data).then(res=>{
        if(res.data){
          this.$emit("valid",true)
        }else{
          this.refreshCaptcha();
        }

      })
    },
    handleDragStart(e) {
      this.startSlidingTime = new Date();
      // 获取拖拽起始位置坐标
      this.originX = e.clientX || e.touches[0].clientX;
      this.originY = e.clientY || e.touches[0].clientY;
      this.isMouseDown = true;
    },
    handleDragMove(ev){
      if (!this.isMouseDown) return false;
      const w = 206;//滑道宽度
      // 获取拖拽移动的距离
      const eventX = ev.clientX || ev.touches[0].clientX;
      const eventY = ev.clientY || ev.touches[0].clientY;
      let moveX = eventX - this.originX;
      let moveY = eventY - this.originY;
      this.trackList.push({x: moveX, y: moveY, t: (new Date().getTime() -this. startSlidingTime.getTime())});
      if (moveX < 0)
        moveX = 0;
      else if (moveX > w)
        moveX = w;
      this.$refs.sliderImg.style.transform = 'translate(' + moveX + 'px, 0px)';
      this.$refs.sliderBtn.style.transform = 'translate(' + moveX + 'px, 0px)';
    },
    handleDragEnd(ev){
      if (!this.isMouseDown) return false;
      this.isMouseDown = false;
      const eventX = ev.clientX || ev.changedTouches[0].clientX;
      if (eventX === this.originX) return false;
      this.entSlidingTime = new Date();
      this.$refs.sliderImg.style.transform = 'translate(0px, 0px)';
      this.$refs.sliderBtn.style.transform = 'translate(0px, 0px)';
      this.valid();
    }
  },
  mounted() {
    this.refreshCaptcha();
    window.addEventListener('touchstart', this.handleDragStart);
    window.addEventListener('touchmove', this.handleDragMove);
    window.addEventListener('touchend', this.handleDragEnd);

    window.addEventListener('mousedown', this.handleDragStart);
    window.addEventListener('mousemove', this.handleDragMove);
    window.addEventListener('mouseup', this.handleDragEnd);
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

.bg-img-div {
  width: 100%;
  height: 100%;
  position: absolute;
  transform: translate(0px, 0px);
}

.slider-img-div {
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
}

</style>

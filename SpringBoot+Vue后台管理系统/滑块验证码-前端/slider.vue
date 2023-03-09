<template>
  <div class="slider">
    <div class="mask">
      <div class="container">
        <div class="title">
          <div class="text">
            <span>请完成下列验证后继续</span>
          </div>
          <div class="button-group">
            <img
              src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAE2UlEQVRoQ+1YXWwUVRT+zsw2Qktn1ljatEjjD+4WJILpg0rE4ItGn0QMyGMTE/kJTUwFElt2ZmopQWhIMNjyRKIPiH/hScXEiJEgL/4mamcwUSG2UCvuzAJt7e4cc6ddFaTMHTq70qTztnvPPef7zt899xJm+EczHD9mCfzfEZyNQNwRaD7IFSPnLy/FbZWnv99MF8P033QRSFvuDmbuJOBL20w2zzwCpnuEwWtnLIGU6X4O8IMEHLXN5OqyRcBgVg7vzK1AwX+KGM0ANQDcwAQVwAVi+oWAU6Ti46Vp7djba6lwLXBp0/2VwQ0getUx9NaSE1h1iOcMns21ss9tANeGGRTrBDrHCnrnzNV6vt1Kl4p7RAHnBr1RgBVFUbb2Z7S9YfqmVcRNVm6Nj8I+MBYGwAiXmHFMIXzkg85UkDpQQD7BwN0M3AWfloGwGsy3FIlARYu9Q/9Q/G7aNXKHPzb2U7CmYJ2dSb5VEgLMTGkr1wX4LwUGiM4T2Kpv1A8db6HR6xm9vzs3f2S88JwPbAZjAYgKBN5iG8nexV25Rwr5wqdiv5qgh37o0E/FTkCAb+r0DjPzukkv9qnzta0yPfvfYB7Yz9ofF9xDAJ4W/ytE+3wVXyHPrwd651YssLdXDcROIGV6O4XnCZQHcavw3PWMLOn0VvrghUQ8xJw4n1AxtPiequFiETdZXpsP3g1mlUAnGbyCiMafzWhzLCI/VgJBznPhncl83xQGvvkgV14852WZueJKIOSD8DuAIYIgRrcCvLwoQ8DPtpm8Mwz8RAZIfqLbDJxxHVGwBOqzTX2jzNa06R5gQjNNdKhaZlSF76MTjqmvDJeLQCDd6W1j398tCjZRoy2KmvNFMPft4aqx0dFahf6s85kEoVpivw5Mt4v0AagSRH2OofXERiA4pCxvUPR5IoSmjozhuGSkUijV5T2MvP+Z6PP1jXpNWKuMC5yMHjkCVnYvGG0A3nPM5BoZxeWSkSKQNrOfMLBKIWzoN5IHywVOxo4kAddmcApETzqG/oGM4huVEfX2puVZTHAcQ38jTI8kgWyOgXkVSmL5d5l534Qpnc76vbvcReNjfJqAy7aZDG25Nx+BzovLxv3810TI2UZSC3OGJIHypVDKcp8A8/sEcmxTT8dEoHxF3GRln/cZfQQct83ko7EQSJWxjabM7LvBhErocYzki/EQKNNBFtzuzrjDwbyUUFY6HdqJWAiUa5RIW9mNzHgNoKH1hlYf6zgd1zA3lUeXHOB5+WHvRzDXkaJstzPaK2Hev/FxmqjXNvRNMgZkZdKm28vgDSCcbWjUU7LzllQbLYK44kKjYKOdSfbJArye3D+pI66W6jP9RrUoZKkvEgGhMW25XczcHlwpFd4yXRICPJj2MzgBKN2OqbVLIZ8UikzgP5d6ol61RtsW9YIjcr7wm7cnSJvgYYOO9Ge09UTEJSUglE+SeFlEIjAW4VllslW2MMgQBTsBVum2jeqOqOAjFfG1vCLzsCX2jXOhQQE3+ozHiPD43/diwlkF6gtRcv5qHJFT6GoFN/K0KPo8KdRTv7B6v2y3mSqtpk2gqHiqx92JdRoAeIAJX0BVjq5vrz4pc0jJ1EJsBGSMlUJmlkApvBpF52wEonirFLKzESiFV6PonI1AFG+VQvYvqQFST/EC5cgAAAAASUVORK5CYII="
              @click="reset"
            />
            <img
              src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAB3klEQVRoQ+2Yy0oEMRBFz/hABf0Gda2gO9349W50p6Br9RsUVHxS0JEwZCaV5EZp7IFedbpyT91KUpMZI//NRq6fCeCvHZwcmBxozMC/KqFV4BP4akya53Ob68Mz0OvAPrALvAA3wJMneMWYbeAQ2AQegLtcDA+AZeMsCvQGXHeAMPFHwHo013nOCQ+AjTkZshJiqyFS4s3ty1zJegBMtE1wDKxF2XkHrgROpMRbbHP5UVFCIUYPiCbxJszrQA+IZvE1AKpyWuSmq2zisip1QOGETHytAy0Q8nVU60AOIlUKcvGtDpRA7AyHlHwbbnUghpg/RcNebmPsnVy8yoEchL3vIl4NELbYeSfiXU91ev/EVJVQLDJ1QNl7ufgeDljM1IINAMUHlbIXysVaJj58627SPJOpHUhl3tpu+8U9vhRCtQYWibeSCdtoFwgFwDLx4a+npPNMlVUrgEd87pxoWtgtACXiu0HUAtSI7wJRA9AiXg5RCqAQL4UoAVCKl0F4AXqIl0B4AGzMKbAR7cO/cbH1ClwoLrZGf7Void8bnmfgVnAbt6hXsxP7ANgC7odnaV/nKaEQYGW4Xvc2ii3j3HOVALQI6vbtBNAttc7AkwPORHUbNjnQLbXOwKN34BvKiqMxJwSPZAAAAABJRU5ErkJggg=="
              @click="close"
            />
          </div>
        </div>
        <div class="img">
          <div class="backgroup-img">
            <img
              class="inner-bg-img"
              :src="backgroupImg"
            />
          </div>
          <div
            class="move-img"
            :style="{left: `${moveX}px`}"
          >
            <img
              class="inner-mv-img"
              :src="moveImg"
            />
          </div>
        </div>
        <div class="slide">
          <div
            class="slider-mask"
            :style="{width: `${blcokLeft}px`}"
          >
            <div
              class="block"
              ref="block"
              @mousedown="start"
              :style="{left: `${blcokLeft}px`}"
            >
              <span class="yidun_slider_icon"></span>
            </div>
          </div>
        </div>
        <div
          class="loading"
          v-if="loading"
        >
          <span>loading...</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// =========================================
// 父组件需要提供的方法 名称
// =========================================

/**
 * 获取滑块图片方法
 */
const GET_IMG_FUN = "getImg";
/**
 * 校验滑块图片方法
 */
const VALID_IMG_FUN = "validImg";
/**
 * 滑块窗口关闭事件监听
 */
const CLOST_EVENT_FUN = "close";

export default {
  data() {
    return {
      /**滑块背景图片 */
      backgroupImg: "",
      /**滑块图片 */
      moveImg: "",
      /**是否已经移动滑块 */
      startMove: false,
      /**滑块移动距离 */
      blcokLeft: 0,
      /**开始滑动的x轴 */
      startX: 0,
      /**划过的百分比 */
      movePercent: 0,
      /**验证码唯一ID */
      uuid: "",
      /**滑块移动的x轴 */
      moveX: 0,
      /** 加载遮罩标识 */
      loading: false
    };
  },
  props: {
    // 是否开启日志， 默认true
    log: {
      type: Boolean,
      required: false,
      default: true
    }
  },
  mounted() {
    this.getImg();
  },
  methods: {
    /**
     * 打印日志
     */
    printLog(msg, ...optionalParams) {
      if (this.log) {
        if (optionalParams && optionalParams.length > 0) {
          console.info(
            `滑块验证码[${msg}]`,
            optionalParams.length === 1 ? optionalParams[0] : optionalParams
          );
        } else {
          console.info(`滑块验证码[${msg}]`);
        }
      }
    },

    /**
     * 获取滑块图片
     */
    getImg() {
      this.loading = true;
      this.$emit(GET_IMG_FUN, data => {
        this.printLog(GET_IMG_FUN, data);
        this.loading = false;
        if (!data) return;
        console.log("data", data);

        this.backgroupImg = data.captcha.backgroundImage;
        this.moveImg = data.captcha.sliderImage;
        this.uuid = data.id;
      });
    },
    /**
     * 校验图片
     */
    validImg() {
      this.printLog(`滑块抬起`, this.movePercent);
      this.$emit(VALID_IMG_FUN, this.movePercent, this.uuid, data => {
        this.printLog(VALID_IMG_FUN, data);
        if (data === false) {
          this.reset();
        }
      });
    },
    /**
     * 重新生成图片
     */
    reset() {
      this.getImg();
      this.moveX = 0;
      this.movePercent = 0;
      this.startX = 0;
      this.blcokLeft = 0;
    },
    /**
     * 按钮关闭事件
     */
    close() {
      this.printLog("关闭按钮触发");
      this.$emit(CLOST_EVENT_FUN);
    },
    /**
     * 开始滑动
     */
    start(e) {
      this.startX = e.pageX;
      this.startMove = true;
      window.addEventListener("mousemove", this.move);
      window.addEventListener("mouseup", this.up);
    },
    /**
     * 滑块滑动事件
     */
    move(e) {
      if (!this.startMove) return;
      const moveX = e.pageX - this.startX;
      const movePercent = moveX / 280;
      if (moveX <= 0) {
        this.blcokLeft = 0;
        this.moveX = 0;
        this.movePercent = 0;
      } else if (moveX >= 0 && moveX <= 235) {
        this.blcokLeft = moveX;
        this.moveX = moveX;
        this.movePercent = movePercent;
      } else if (moveX >= 235) {
        this.blcokLeft = 235;
        this.moveX = 235;
        this.movePercent = movePercent;
      }
    },
    /**
     * 滑块鼠标抬起事件
     */
    up(e) {
      window.removeEventListener("mousemove", this.move);
      window.removeEventListener("mouseup", this.up);
      if (!this.startMove) return;
      this.startMove = false;
      this.validImg();
    }
  },
  /**
   * 销毁事件
   */
  beforeDestroy() {
    window.removeEventListener("mousemove", this.move);
    window.removeEventListener("mouseup", this.up);
  }
};
</script>

<style lang="scss" scoped>
.slider-mask {
  position: absolute;
  left: 0;
  top: 0;
  height: 40px;
  border: 0 solid #1991fa;
  background: #d1e9fe;
  border-radius: 2px;
}
.yidun_slider_icon {
  position: absolute;
  top: 50%;
  margin-top: -6px;
  left: 50%;
  margin-left: -6px;
  width: 14px;
  height: 10px;
  background-image: url(https://cstaticdun.126.net//2.13.7/images/icon_light.4353d81.png);
  background-position: 0 -13px;
  background-size: 32px 544px;
}
.inner-mv-img,
.inner-bg-img,
.title {
  -moz-user-select: none;
  -webkit-user-select: none;
  -ms-user-select: none;
  -khtml-user-select: none;
  user-select: none;
}
.slider {
  .mask {
    display: block;
    z-index: 998;
    background: rgba(0, 0, 0, 0);
    width: 310px;
    height: 280px;
  }
  .container {
    position: absolute;
    z-index: 999;
    width: 310px;
    height: 280px;
    margin: auto;
    background: rgba(255, 255, 255, 1);
    border-radius: 6px;
    box-shadow: 0px 0px 11px 0px rgba(153, 153, 153, 1);
    box-sizing: border-box;
    padding: 17px 15px;
    .title {
      font-size: 14px;
      color: #333;
      display: flex;
      justify-content: space-between;
      .button-group {
        img {
          width: 25px;
          height: 25px;
          cursor: pointer;
        }
      }
    }
    .img {
      width: 280px;
      height: 180px;
      position: relative;
      img {
        width: 100%;
      }
      .backgroup-img {
        position: absolute;
        left: 0;
        top: 0;
        width: 100%;
      }
      .move-img {
        width: 52.20338981px;
        position: absolute;
        left: 0;
        top: 0;
      }
    }
    .slide {
      width: 100%;
      height: 40px;
      border: 1px solid #e4e7eb;
      background-color: #f7f9fa;
      box-sizing: border-box;
      position: relative;
      &::before {
        position: absolute;
        content: "按住左边按钮移动完成上方拼图";
        display: flex;
        justify-content: center;
        align-items: center;
        font-size: 12px;
        color: #999;
        width: 100%;
        height: 100%;
        text-indent: 50px;
      }
      .block {
        width: 40px;
        height: 38px;
        background-color: #fff;
        box-shadow: 0 0 3px rgba(0, 0, 0, 0.3);
        display: flex;
        justify-content: center;
        align-items: center;
        position: absolute;
        left: 0;
        top: 0;
        cursor: pointer;
        background-size: 30px;
        background-repeat: no-repeat;
        background-position: center;
      }
    }
    .block:hover {
      background-color: #1991fa;
    }
    .block:hover .yidun_slider_icon {
      background-image: url(https://cstaticdun.126.net//2.13.7/images/icon_light.4353d81.png);
      background-position: 0 0;
      background-size: 32px 544px;
    }
    .loading {
      width: 100%;
      height: 100%;
      background: rgba(0, 0, 0, 0.3);
      position: absolute;
      top: 0;
      left: 0;
      border-radius: 6px;
      display: flex;
      justify-content: center;
      align-items: center;
      color: #fff;
    }
  }
}
</style>
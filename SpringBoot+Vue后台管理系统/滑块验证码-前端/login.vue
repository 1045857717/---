<template>
  <div class="main">
    <el-tag>教育平台登录临时接口</el-tag>
    <el-form
      :label-position="labelPosition"
      label-width="80px"
      :rules="rules"
      ref="formLabelAlign"
      :model="formLabelAlign"
      v-loading="loading"
    >
      <el-form-item
        label="用户名"
        prop="username"
      >
        <el-input v-model="formLabelAlign.username"></el-input>
      </el-form-item>
      <el-form-item
        label="密码"
        prop="password"
      >
        <el-input v-model="formLabelAlign.password"></el-input>
      </el-form-item>
      <el-form-item
        label="类型"
        prop="type"
      >
        <el-input v-model.number="formLabelAlign.type"></el-input>
      </el-form-item>
      <el-button
        type="primary"
        @click="onSubmit('formLabelAlign')"
      >登录</el-button>
    </el-form>
    <div
      class="islider"
      v-if="showSlider">
      <Slider
        @getImg="getImg"
        @validImg="validImg"
        @close="closeCaptcha"
        :log="true"
      ></Slider>
    </div>

  </div>
</template>

<script>
import Slider from "@/views/slider";
// import Slider2 from "@/views/slider2";
export default {
  components: {
    Slider,
    // Slider2
  },
  data() {
    return {
      labelPosition: "left",
      formLabelAlign: {
        username: "",
        password: "",
        type: 3,
        sliderComponentName: "Slider"
      },
      loading: false,
      rules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" }
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" }
        ],
        type: [
          { required: true, message: "请输入类型", trigger: "blur" }
        ]
      },
      showSlider: false
    };
  },
  created() {
    // let ramdom = Math.round(Math.random());
    // if(ramdom === 0) {
    //   this.sliderComponentName = "slider";
    // }else{
    //   this.sliderComponentName = "slider2";
    // }

    this.sliderComponentName = "slider2";
  },
  methods: {
    onSubmit(formLabelAlign) {
      // let ramdom = Math.round(Math.random());
      // if(ramdom === 0) {
      //   this.sliderComponentName = "slider";
      // }else{
      //   this.sliderComponentName = "slider2";
      // }
      let validated = false;
      this.$refs[formLabelAlign].validate(valid => {
        if(valid) {
          validated= true;
        }
      });
      if(!validated) {
        return;
      }
      this.showSlider = true;
    },
    getImg(callback) {
      this.$axios.get("http://192.168.3.7:8081/captcha").then(data => {
        console.log("aaa", data.data.data);
        callback(data.data.data);
      }).catch(ex => {
        callback(ex.data);
      });
    },
    closeCaptcha() {
      this.showSlider = false;
    },
    validImg(movePercent, id, callback) {
      const postData = {
        percentage: movePercent,
        id: id,
        form: {
          userName: this.formLabelAlign.username,
          password: this.formLabelAlign.password,
          type: this.formLabelAlign.type
        }
      };

      this.loading = true;
      this.$axios(`http://192.168.3.8:8081/captcha/valid/${id}/${movePercent}`).then(d => {
        this.$message.success(d)
        callback(false)
        this.showSlider = true;
      }).catch(ex => {
        this.loading = false;
        this.$message.success(JSON.stringify(ex.data))
        callback(false)
        this.showSlider = true;
      })
    //  this.$axios
    //     .post(`/login`, postData)
    //     .then(res => {
    //       this.loading = false;
    //       callback(true);
    //       this.showSlider = false;
    //       if (res.data.code === "00000") {
    //         this.$message({
    //           showClose: true,
    //           message: `登录成功: token已保存至粘贴板\r\n${res.data.data.token}`,
    //           type: 'success'
    //         });
    //         this.$copyText(res.data.data.token).then(res => {

    //         }).catch(res => {
    //           this.$message.warning("复制token到粘贴板失败");
    //         });
            
    //       }else{
    //         this.$message.error(`服务器错误: ${res.data.msg}`);
    //       }
    //     })
    //     .catch(err => {
    //       console.error(err);
    //       this.loading = false;
    //       if(err.data.code === 'D1001') {
    //         this.$message.warning(`请正确拼合验证码`);
    //         this.showSlider = true;
    //         callback(false);
    //       }else{
    //         this.$message.error(`服务器错误: ${err.data.msg}`);
    //         callback(true);
    //         this.showSlider = false;
    //       }
    //     });
    }
  }
};
</script>

<style lang="scss" scoped>
.el-tag {
  margin-top: 200px;
}
.el-button--primary {
  width: 100%;
}
.main {
  display: flex;
  // margin-top: 200px;
  height: 100%;
  flex-direction: column;
  align-items: center;
  position: relative;
}
.el-form {
  width: 500px;
}
.islider {
  width: 278px;
  height: 285px;
  // display: flex;
  // align-items: center;
  // margin: 0 auto;
  // margin: 0 auto;
  position: absolute;
  top: 0;
}
</style>
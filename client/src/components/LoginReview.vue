<template>
  <section class="gradient-custom">
    <div class="container py-xl-5 h-75">
      <div class="row d-flex justify-content-center align-items-center h-100">
        <div class="col-12 col-md-8 col-lg-6 col-xl-5">
          <div class="card bg-dark text-white" style="border-radius: 2rem">
            <div class="card-body p-5 text-center">
              <h2 class="fw-bold mb-2 text-uppercase">Login</h2>
              <p class="text-white-50 mb-3">Indique sus datos de acceso:</p>
              <div class="form-outline form-white mb-4">
                <div class="container">
                  <div class="form-floating">
                    <input
                      type="text"
                      id="login"
                      class="form-control"
                      v-model="login"
                      style="margin-bottom: 5%"
                    />
                    <label class="form-label" for="login">Login</label>
                  </div>
                </div>
                <div class="form-outline form-white">
                  <div class="container">
                    <div class="form-floating">
                      <input
                        type="password"
                        id="contrasena"
                        v-model="contrasena"
                        class="form-control"
                      />
                      <label class="form-label" for="contrasena"
                        >Contraseña</label
                      >
                      <div v-if="errorCode === 401">
                        <h6 style="color: red; margin-top: 1%">
                          Login o contraseña inválida
                        </h6>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <button
                class="btn btn-outline-light btn-lg px-5 btn-success"
                type="button"
                v-on:click="Login"
              >
                Login
              </button>
              <div style="margin-top: 5%">
                <p class="mb-0">
                  ¿No te has registrado todavía?
                  <a href="/register" class="text-white-50 fw-bold">Registro</a>
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>
<script>
import auth from "@/common/auth";
export default {
  name: "LoginUser",
  data() {
    return {
      login: "",
      contrasena: "",
      errorMessage: "",
      errorCode: 0,
    };
  },
  methods: {
    async Login() {
      const user = {
        login: this.login,
        contrasena: this.contrasena,
      };
      this.errorCode = 0;
      if (user.login.length === 0) {
        this.errorCode = 1;
        this.errorMessage = "El login no puede estar vacio";
      } else if (user.contrasena.length === 0) {
        this.errorCode = 2;
        this.errorMessage = "La contraseña no puede estar vacia";
      } else {
        try {
          await auth.login(user);
          this.$router.push({
            name: "crearReview",
            params: { link: this.$route.params.linkReview },
          });
        } catch (error) {
          switch (error.response.status) {
            case 401:
              this.errorCode = error.response.status;
              this.errorMessage = "Login o contraseña incorrectos";
              break;
            case 500:
              this.errorCode = error.response.status;
              this.errorMessage =
                "Ha ocurrido un error, pruebe de nuevo mas tarde";
              break;
          }
        }
      }
    },
  },
};
</script>
<style scoped>
.gradient-custom {
  background: linear-gradient(to right, rgb(182, 244, 146), rgb(51, 139, 147));
}
.form-label {
  color: black;
}
</style>

<template>
  <form novalidate>
    <section class="gradient-custom">
      <div class="container py-4 h-75">
        <div class="row d-flex justify-content-center align-items-center h-70">
          <div class="col-12 col-md-8 col-lg-6 col-xl-8">
            <div class="card bg-dark text-white" style="border-radius: 1rem">
              <div class="card-body p-5 text-center">
                <h2 class="fw-bold mb-2 text-uppercase">Registro</h2>
                <p class="text-white-50 mb-3">
                  Rellena los siguientes campos para crear una cuenta
                </p>
                <div class="form-outline form-white mb-4">
                  <div class="container">
                    <div class="form-floating">
                      <input
                        type="text"
                        id="nombre"
                        v-model="nombre"
                        class="form-control has-validation"
                        :class="{ errorInput: hasErrorsNombre }"
                      />
                      <label class="form-label" for="nombre"
                        >Nombre y apellidos</label
                      >
                      <div
                        style="color: red"
                        class="nombreInvalido"
                        v-if="hasErrorsNombre"
                      >
                        Indica tu nombre y apellidos
                      </div>
                    </div>
                  </div>
                </div>
                <div class="row">
                  <div class="col">
                    <div class="form-outline form-white mb-4">
                      <div class="container">
                        <div class="form-floating">
                          <input
                            type="text"
                            id="login"
                            class="form-control"
                            v-model="login"
                            :class="{
                              errorInput:
                                hasErrorsLoginVacio ||
                                loginRepetido ||
                                hasErrorsLogin,
                            }"
                          />
                          <label class="form-label" for="login">Login</label>
                          <div
                            v-if="
                              loginRepetido ||
                              hasErrorsLoginVacio ||
                              hasErrorsLogin
                            "
                          >
                            <h6 style="color: red" v-if="loginRepetido">
                              Login ya existente, inténtelo con otro diferente
                            </h6>
                            <h6 style="color: red" v-if="hasErrorsLoginVacio">
                              Indique un login válido
                            </h6>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="col">
                    <div class="form-outline form-white mb-4">
                      <div class="container">
                        <div class="form-floating">
                          <input
                            type="password"
                            id="contrasena"
                            v-model="contrasena"
                            class="form-control"
                            :class="{
                              errorInput:
                                hasErrorsContrasenaVacia || hasErrorsContrasena,
                            }"
                          />
                          <label class="form-label" for="contrasena"
                            >Contraseña</label
                          >
                          <div
                            v-if="
                              hasErrorsContrasena || hasErrorsContrasenaVacia
                            "
                          >
                            <h6
                              style="color: red"
                              v-if="
                                hasErrorsContrasenaVacia || hasErrorsContrasena
                              "
                            >
                              Indique una contraseña válida
                            </h6>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="row">
                  <div class="col">
                    <div class="form-outline form-white mb-4">
                      <div class="container">
                        <div class="form-floating">
                          <input
                            type="email"
                            id="email"
                            v-model="email"
                            class="form-control"
                            :class="{
                              errorInput:
                                emailRepetido ||
                                hasErrorsEmailVacio ||
                                hasErrorsEmail,
                            }"
                          />
                          <label class="form-label" for="email">Email</label>
                          <div
                            v-if="
                              hasErrorsEmail ||
                              hasErrorsEmailVacio ||
                              emailRepetido
                            "
                          >
                            <h6
                              style="color: red"
                              v-if="hasErrorsEmailVacio || hasErrorsEmail"
                            >
                              Indique un email válido
                            </h6>
                            <h6 style="color: red" v-if="emailRepetido">
                              El email ya corresponde a una cuenta. Indique uno
                              diferente
                            </h6>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="col">
                    <div class="form-outline form-white mb-4">
                      <div class="container">
                        <div class="form-floating">
                          <input
                            type="number"
                            id="telefono"
                            v-model="telefono"
                            class="form-control"
                            :class="{
                              errorInput:
                                hasErrorsTelefono || hasErrorsTelefonoVacio,
                            }"
                          />
                          <label class="form-label" for="telefono"
                            >Telefono</label
                          >
                          <div
                            v-if="hasErrorsTelefono || hasErrorsTelefonoVacio"
                          >
                            <h6 style="color: red">
                              Indique un teléfono válido
                            </h6>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <button
                  class="btn btn-outline-light btn-lg px-5 btn-success"
                  type="button"
                  v-on:click="registerUser"
                >
                  Registrarse
                </button>
                <div>
                  <p class="mb-0">
                    ¿Ya tienes una cuenta?
                    <a href="/login" class="text-white-50 fw-bold">Login</a>
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </form>
</template>

<script>
import AccountRepository from "@/repositories/AccountRepository";
import auth from "@/common/auth";
export default {
  name: "RegistroUsuario",
  data() {
    return {
      errorCode: 0,
      errorMessage: "",
      login: "",
      contrasena: "",
      email: "",
      nombre: "",
      telefono: "",
      hasErrorsLoginVacio: false,
      hasErrorsLogin: false,
      hasErrorsContrasena: false,
      hasErrorsContrasenaVacia: false,
      hasErrorsEmail: false,
      hasErrorsEmailVacio: false,
      hasErrorsTelefono: false,
      hasErrorsNombre: false,
      hasErrorsTelefonoVacio: false,
      hasErrorsNombreVacio: false,
      loginRepetido: false,
      emailRepetido: false,
    };
  },
  methods: {
    comprobacionesDatos(user) {
      this.hasErrorsLogin = false;
      this.hasErrorsContrasena = false;
      this.hasErrorsEmail = false;
      this.hasErrorsNombre = false;
      this.hasErrorsTelefono = false;
      this.hasErrorsLoginVacio = false;
      this.hasErrorsContrasenaVacia = false;
      this.hasErrorsEmailVacio = false;
      this.hasErrorsNombreVacio = false;
      this.hasErrorsTelefonoVacio = false;
      this.errorCode = 0;
      if (
        (user.login.length < 3 && user.login.length !== 0) ||
        //dividir la string usando espacios en blanco como delimitador (split(" ")) y luego volver a unirla con una string vacía(join(""))
        user.login.valueOf().split(" ").join("") !== user.login.valueOf()
      ) {
        this.errorCode = 1;
        this.hasErrorsLogin = true;
        window.scrollTo(0, 0);
      }
      if (user.login.length === 0) {
        this.errorCode = 1;
        this.hasErrorsLoginVacio = true;
        window.scrollTo(0, 0);
      }
      if (
        (user.contrasena.length < 8 &&
          user.contrasena.length !== 0 &&
          !user.contrasena.match(/[A-z]/) &&
          !user.contrasena.match(/[A-Z]/) &&
          !user.contrasena.match(/\d/)) ||
        user.contrasena.valueOf().split(" ").join("") !==
          user.contrasena.valueOf()
      ) {
        this.errorCode = 1;
        this.hasErrorsContrasena = true;
        window.scrollTo(0, 0);
      }
      if (user.contrasena.length === 0) {
        this.errorCode = 1;
        this.hasErrorsContrasenaVacia = true;
        window.scrollTo(0, 0);
      }
      if (user.email.length === 0) {
        this.errorCode = 1;
        this.hasErrorsEmailVacio = true;
        window.scrollTo(0, 0);
      } else {
        let emailRegex =
          /^[-\w.%+]{1,64}@(?:[A-Z0-9-]{1,63}\.){1,125}[A-Z]{2,63}$/i;
        if (!emailRegex.test(user.email)) {
          this.errorCode = 1;
          this.hasErrorsEmail = true;
          window.scrollTo(0, 0);
        }
      }
      if (user.nombre.length === 0) {
        this.errorCode = 1;
        this.hasErrorsNombre = true;
        window.scrollTo(0, 0);
      }
      if (user.telefono.length === 0) {
        this.errorCode = 1;
        this.hasErrorsTelefonoVacio = true;
        window.scrollTo(0, 0);
      } else {
        if (String(user.telefono).length !== 9) {
          this.errorCode = 1;
          this.hasErrorsTelefono = true;
          window.scrollTo(0, 0);
        }
      }
    },
    async registerUser() {
      const user = {
        login: this.login,
        contrasena: this.contrasena,
        email: this.email,
        nombre: this.nombre,
        telefono: this.telefono,
      };
      this.loginRepetido = false;
      this.emailRepetido = false;
      this.comprobacionesDatos(user);
      if (this.errorCode === 0) {
        try {
          const login = {
            login: this.login,
            contrasena: this.contrasena,
          };
          await AccountRepository.registerAccount(user);
          await auth.login(login);
          this.$router.push({
            name: "/home",
          });
        } catch (error) {
          switch (error.response.status) {
            case 400:
              if (error.response.data.message.includes("login")) {
                this.errorCode = error.response.status;
                this.loginRepetido = true;
                window.scrollTo(0, 0);
                break;
              }
              if (error.response.data.message.includes("email")) {
                this.errorCode = error.response.status;
                this.emailRepetido = true;
                window.scrollTo(0, 0);
                break;
              }
              break;
            case 500:
              this.errorCode = error.response.status;
              this.errorMessage = "Error al registrar, pruebe en un rato";
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
  /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
  background: linear-gradient(to right, rgb(182, 244, 146), rgb(51, 139, 147));
}
.form-label {
  color: black;
}
input.errorInput {
  border-color: red;
  background-color: indianred;
}
</style>

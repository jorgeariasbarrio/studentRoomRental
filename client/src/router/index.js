import { createRouter, createWebHistory } from "vue-router";
import RegisterUser from "@/components/RegisterUser";
import LoginUser from "@/components/LoginUser";
import ErrorNotFoundView from "@/components/ErroNotFoundView";
import { getStore } from "@/common/store";
import auth from "@/common/auth";
import CrearPiso from "@/components/CrearPiso";
import ListaHabitaciones from "@/components/ListaHabitaciones.vue";
import MisFavoritos from "@/components/MisFavoritos";
import MisAnuncios from "@/components/MisAnuncios";
import EditarPiso from "@/components/EditarPiso";
import AnadirHabitacion from "@/components/AnadirHabitacion";
import ModificarHabitacion from "@/components/ModificarHabitacion";
import MapaHabitaciones from "@/components/MapaHabitaciones";
import HabitacionAlquilada from "@/components/HabitacionAlquilada";
import MiPerfil from "@/components/MiPerfil";
import MisReviews from "@/components/MisReviews";
import crearReview from "@/components/CrearReview";
import LoginReview from "@/components/LoginReview";
import RegisterForReview from "@/components/RegisterForReview";
import DetalleHabitacion from "@/components/DetalleHabitacion";
import ListaHabitacionesUsuario from "@/components/ListaHabitacionesUsuario";
import AdminOptions from "@/components/AdminOptions";
import AdminOptionsHabitaciones from "@/components/AdminOptionsHabitaciones";
import { useToast } from "vue-toastification";

const routes = [
  {
    path: "/",
    name: "home",
    component: ListaHabitaciones,
    meta: { public: true },
  },
  {
    path: "/:idHabitacion(\\d+)",
    name: "DetalleHabitacion",
    component: DetalleHabitacion,
    meta: { public: true },
  },
  {
    path: "/usuario/:id(\\d+)",
    name: "PerfilUsuario",
    component: ListaHabitacionesUsuario,
    meta: { public: true },
  },
  {
    path: "/register",
    name: "register",
    component: RegisterUser,
    meta: { public: true, isLoginPage: true },
  },
  {
    path: "/login",
    name: "login",
    component: LoginUser,
    meta: { public: true, isLoginPage: true },
  },
  {
    path: "/loginReview/:linkReview",
    name: "loginReview",
    component: LoginReview,
    meta: { public: true, isLoginPage: true },
  },
  {
    path: "/registerForReview/:linkReview",
    name: "registerForReview",
    component: RegisterForReview,
    meta: { public: true, isLoginPage: true },
  },
  {
    path: "/crearPiso",
    name: "crearPiso",
    component: CrearPiso,
    meta: { public: false },
  },
  {
    path: "/favoritos",
    name: "favoritos",
    component: MisFavoritos,
    meta: { public: false },
  },
  {
    path: "/misAnuncios",
    name: "misAnuncios",
    component: MisAnuncios,
    meta: { public: false },
  },
  {
    path: "/editarPiso/:idPiso(\\d+)",
    name: "editarPiso",
    component: EditarPiso,
    meta: { public: false },
  },
  {
    path: "/anadirHabitacion/:idPiso(\\d+)",
    name: "anadirHabitacion",
    component: AnadirHabitacion,
    meta: { public: false },
  },
  {
    path: "/modificarHabitacion/:id(\\d+)",
    name: "modificarHabitacion",
    component: ModificarHabitacion,
    meta: { public: false },
  },
  {
    path: "/mapaHabitaciones",
    name: "mapaHabitaciones",
    component: MapaHabitaciones,
    meta: { public: true },
  },
  {
    path: "/habitacionAlquilada/:idHabitacion(\\d+)",
    name: "habitacionAlquilada",
    component: HabitacionAlquilada,
    meta: { public: false },
  },
  {
    path: "/miPerfil",
    name: "miPerfil",
    component: MiPerfil,
    meta: { public: false },
  },
  {
    path: "/misReviews",
    name: "MisReviews",
    component: MisReviews,
    meta: { public: false },
  },
  {
    path: "/reviews/:link",
    name: "crearReview",
    component: crearReview,
    meta: { public: true },
  },
  {
    path: "/admin",
    name: "AdminOptions",
    component: AdminOptions,
    meta: { public: false, authority: "ADMIN" },
  },
  {
    path: "/admin/anuncios",
    name: "AdminOptionsHabitaciones",
    component: AdminOptionsHabitaciones,
    meta: { public: false, authority: "ADMIN" },
  },
  {
    path: "/:catchAll(.*)*",
    component: ErrorNotFoundView,
    meta: { public: true },
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

router.beforeEach((to, from, next) => {
  // Lo primero que hacemos antes de cargar ninguna ruta es comprobar si
  // el usuario está autenticado (revisando el token)
  auth.isAuthenticationChecked.finally(() => {
    // por defecto, el usuario debe estar autenticado para acceder a las rutas
    const requiresAuth = !to.meta.public;

    const requiredAuthority = to.meta.authority;
    const userIsLogged = getStore().state.user.logged;
    const loggedUserAuthority = getStore().state.user.authority;
    const toast = useToast();

    if (requiresAuth) {
      // página privada
      if (userIsLogged) {
        if (requiredAuthority && requiredAuthority !== loggedUserAuthority) {
          // usuario logueado pero sin permisos suficientes, le redirigimos a la página de login
          toast.error(
            "Acceso prohibido para el usuario actual; intenta autenticarte de nuevo",
            {
              position: "top-right",
              timeout: 10000,
              closeOnClick: true,
              pauseOnFocusLoss: true,
              pauseOnHover: true,
              draggable: true,
              draggablePercent: 0.6,
              showCloseButtonOnHover: false,
              hideProgressBar: true,
              closeButton: "button",
              icon: true,
              rtl: false,
            }
          );
          auth.logout();
          next("/login");
        } else {
          // usuario logueado y con permisos adecuados
          next();
        }
      } else {
        // usuario no está logueado, no puede acceder a la página
        toast.error("Esta página requiere autorización", {
          position: "top-right",
          timeout: 10000,
          closeOnClick: true,
          pauseOnFocusLoss: true,
          pauseOnHover: true,
          draggable: true,
          draggablePercent: 0.6,
          showCloseButtonOnHover: false,
          hideProgressBar: true,
          closeButton: "button",
          icon: true,
          rtl: false,
        });
        next("/login");
      }
    } else {
      // página pública
      if (userIsLogged && to.meta.isLoginPage) {
        // si estamos logueados no hace falta volver a mostrar el login
        next({ name: "home", replace: true });
      } else {
        next();
      }
    }
  });
});

export default router;

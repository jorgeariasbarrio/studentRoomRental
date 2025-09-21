import "bootstrap/dist/css/bootstrap.css";
import { createApp } from "vue";
import "@/plugins/leaflet";
import App from "./App.vue";
import router from "./router";
import { library } from "@fortawesome/fontawesome-svg-core";
import { faHatWizard } from "@fortawesome/free-solid-svg-icons";
import { faHeart } from "@fortawesome/free-solid-svg-icons";
import { faHeart as faHeartRegular } from "@fortawesome/free-regular-svg-icons";
import { faPenToSquare } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import "bootstrap-icons/font/bootstrap-icons.css";
import Toast from "vue-toastification";
import "vue-toastification/dist/index.css";

library.add(faHatWizard);
library.add(faHeart);
library.add(faHeartRegular);
library.add(faPenToSquare);
let app = createApp(App);
app.component("font-awesome-icon", FontAwesomeIcon);

app.use(Toast, {
  transition: "Vue-Toastification__bounce",
  maxToasts: 20,
  newestOnTop: true,
});
app.use(router).mount("#app");

import "bootstrap/dist/js/bootstrap.js";

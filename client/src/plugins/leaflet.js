import "leaflet";

import "leaflet/dist/leaflet.css";

import "leaflet.markercluster/dist/MarkerCluster.css";
import "leaflet.markercluster/dist/MarkerCluster.Default.css";
import "leaflet.markercluster/dist/leaflet.markercluster.js";

import iconDefault from "leaflet/dist/images/marker-icon.png";

import iconShadow from "leaflet/dist/images/marker-shadow.png";

import retinaUrl from "leaflet/dist/images/marker-icon-2x.png";

import { Icon } from "leaflet";

delete Icon.Default.prototype._getIconUrl;

Icon.Default.mergeOptions({
  iconRetinaUrl: retinaUrl,

  iconUrl: iconDefault,

  shadowUrl: iconShadow,
});

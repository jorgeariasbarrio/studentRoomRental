import HTTP from "../common/http";

export default {
  async crearReviewInicial(reviewInicial) {
    return (await HTTP.post("/reviews", reviewInicial)).data;
  },
  async asignarReviewACorreo(idReview, email) {
    return (await HTTP.post("/reviews/" + idReview, email)).data;
  },
  async getReviewsByUser(idUsuario) {
    return (await HTTP.get("/reviews/" + idUsuario)).data;
  },
  async getReviewByLink(link) {
    return (await HTTP.get("/reviews/link/" + link)).data;
  },
  async getReviewsByHabitacion(idHabitacion) {
    return (await HTTP.get("/reviews/habitacion/" + idHabitacion)).data;
  },
  async updateReview(review) {
    return (await HTTP.put("/reviews", review)).data;
  },
  async deleteById(id) {
    return await HTTP.delete("/reviews/" + id);
  },
};

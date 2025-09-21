import HTTP from "../common/http";

export default {
  async authenticate(credentials) {
    return (await HTTP.post(`autenticarse`, credentials)).data;
  },
  async getAccount() {
    return (await HTTP.get(`cuenta`)).data;
  },

  async registerAccount(user) {
    return (await HTTP.post(`registro`, user)).data;
  },
  async actualizarAccount(user) {
    return (await HTTP.put("actualizarUsuario/" + user.id, user)).data;
  },
  async actualizarContrasena(id, oldPassword, newPassword) {
    const params = new URLSearchParams();
    if (oldPassword && newPassword) {
      params.append("oldPassword", oldPassword);
      params.append("newPassword", newPassword);
    }
    const paramsStr = params.toString();
    let url = "actualizarContrasena/" + id;
    if (paramsStr) url += "?" + paramsStr;
    const response = await HTTP.put(url);
    return response.data;
  },
  async findById(id) {
    return (await HTTP.get("usuarios/" + id)).data;
  },
};

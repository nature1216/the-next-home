import { authApi } from "./Instance/authInstance";

const addressApi = authApi();

export async function getAddressByMemberId(success, fail) {
  return addressApi.get("/address").then(success).catch(fail);
}

export async function addAddress(address, success, fail) {
  return addressApi.post("/address", address).then(success).catch(fail);
}

export async function updateAddress(addressId, address, success, fail) {
  return addressApi
    .put(`/address/${addressId}`, address)
    .then(success)
    .catch(fail);
}

export async function deleteAddress(addressId, success, fail) {
  return addressApi.delete(`/address/${addressId}`).then(success).catch(fail);
}

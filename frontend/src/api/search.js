import { api } from "./Instance/noAuthInstance";

const searchApi = api();

function searchKeyword(param, success, fail) {
  searchApi
    .get("/dong-code/search", {
      params: {
        sidoCode: param.sidoCode,
        gugunCode: param.gugunCode,
        dongCode: param.dongCode,
        keyword: param.keyword,
      },
    })
    .then(success)
    .catch(fail);
}

function getFilter(param, success, fail) {
  searchApi
    .get("dong-code", {
      params: {
        parentCode: param.param == undefined ? "" : param.param,
      },
    })
    .then(success)
    .catch(fail);
}

export { searchKeyword, getFilter };

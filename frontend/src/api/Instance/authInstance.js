import axios from "axios";

const {VITE_VUE_API_URL} = import.meta.env;
import {useAuthStore} from "@/stores/authStore";
import { toast } from "vue3-toastify";

export const authApi = () => {
  const instance = axios.create({
    baseURL: VITE_VUE_API_URL,
    headers: {
      "Content-Type": "application/json;charset=utf-8",
    },
    withCredentials: true, // 쿠키 포함
  });

  // 요청 인터셉터
  instance.interceptors.request.use(
    (config) => {
      const authStore = useAuthStore();
      const token = authStore.getAuthToken;

      if (token) {
        config.headers["Authorization"] = `Bearer ${token}`;
      }
      return config;
    },
    (error) => {
      return Promise.reject(error);
    }
  );

  // 응답 인터셉터
  instance.interceptors.response.use(
    (response) => {
      return response;
    },
    async (error) => {
      const originalRequest = error.config;

      // 401 오류 발생 시, 리프레시 토큰을 사용하여 액세스 토큰 갱신 시도
      if (error.response.status === 401 && !originalRequest._retry) {
        originalRequest._retry = true;

        try {
          const response = await axios.post(
            `${VITE_VUE_API_URL}/auth/refresh`, // 서버의 토큰 재발급 API
            {},
            {withCredentials: true} // 쿠키 포함 설정
          );

          // 새로 발급받은 Access Token
          const newAccessToken = response.headers["authorization"].replace(
            "Bearer ",
            ""
          );
          const authStore = useAuthStore();

          // 상태 업데이트
          authStore.login(newAccessToken, authStore.getMember, authStore.getMemberId, authStore.getMemberRole); // 새로운 액세스 토큰으로 로그인 상태 갱신
          // 기존 요청 헤더에 새로운 Access Token 추가
          originalRequest.headers["Authorization"] = `Bearer ${newAccessToken}`;

          return axios(originalRequest);
        } catch (refreshError) {
          const authStore = useAuthStore();

          authStore.logout();

        }
      }
      // 403 오류 시, 바로 로그인 페이지로 리다이렉트
      if (error.response.status === 403) {
        const authStore = useAuthStore();

        authStore.logout();

        toast.error("권한이 없습니다.", { autoClose: false });
        setTimeout(() => {
          import('@/router').then(({ default: router }) => {
            router.push({ name: "Login" });
          });
        }, 1000);

      }

      return Promise.reject(error);
    }
  );

  return instance;
};

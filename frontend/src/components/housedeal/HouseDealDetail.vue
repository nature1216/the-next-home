<script setup>
import { ref, defineEmits, defineProps, watch, onMounted, onUpdated } from "vue";
import {
  createFavoriteProperty,
  deleteFavoriteProperty,
  existsFavoritePropertyByAptSeqAndId,
} from "@/api/favoriteProperty";
import { useAuthStore } from "@/stores/authStore";
import HouseDealDuration from "./HouseDealDuration.vue";
import HouseDealGraph from "./item/HouseDealGraph.vue";
import { fetchUnsplashImage } from "@/api/image";
import {toast} from "vue3-toastify";

const emit = defineEmits(["closeDetail"]);
const props = defineProps({
  isVisible: Boolean,
  clickedItem: Object,
});

const authStore = useAuthStore();

const bookmarked = ref(false);
const isVisibleDuration = ref(false);

const selectedSortFilter = ref("date-new");
const localDealList = ref([]);

// image
const loadingImage = ref(true);

onMounted(() => {
  fetchRandomImage();
});

onUpdated(() => {
  if (authStore.isLoggedIn) {
    // 로그인했을 때만 북마크 여부 확인
    existsFavoritePropertyByAptSeqAndId(
      props.clickedItem.aptSeq,
      ({ data }) => {
        bookmarked.value = data;
      },
      (error) => {
        toast.error(error);
      }
    );
  }
})

const closeDetail = () => {
  emit("closeDetail");
  isVisibleDuration.value = false;
  localDealList.value = [];
};

const onClickBookmark = () => {
  if (!bookmarked.value) {
    // 북마크 추가
    createFavoriteProperty(
      {
        aptSeq: props.clickedItem.aptSeq,
      },
      ({ data }) => {
        bookmarked.value = true;
        bookmarked.value = !bookmarked.value;
      },
      (error) => {
        toast.error(error);
      }
    );
  } else {
    // 북마크 삭제
    deleteFavoriteProperty(
      props.clickedItem.aptSeq,
      ({ data }) => {
        bookmarked.value = false;
      },
      (error) => {
        toast.error(error);
      }
    );
  }
};

const onClickDuration = () => {
  isVisibleDuration.value = !isVisibleDuration.value;
};

const sortDealList = () => {
  localDealList.value.sort((a, b) => {
    const priceA = parseFloat(a.dealAmount.replace(/,/g, "")) || 0;
    const priceB = parseFloat(b.dealAmount.replace(/,/g, "")) || 0;
    const dateA = new Date(a.dealYear, a.dealMonth - 1, a.dealDay);
    const dateB = new Date(b.dealYear, b.dealMonth - 1, b.dealDay);

    if (selectedSortFilter.value === "date-new") {
      return dateB - dateA;
    } else if (selectedSortFilter.value === "date-old") {
      return dateA - dateB;
    } else if (selectedSortFilter.value === "price-high") {
      return priceB - priceA;
    } else if (selectedSortFilter.value === "price-low") {
      return priceA - priceB;
    }
  });
};

// `selectedSortFilter` 값 변경 감지
watch(selectedSortFilter, () => {
  sortDealList();
});

watch(
  () => props.clickedItem,
  (newVal) => {
    if (newVal && newVal.dealList) {
      localDealList.value = [...newVal.dealList]; // dealList 복사
      sortDealList(); // 초기 정렬
    } else {
      localDealList.value = []; // clickedItem이 없는 경우 초기화
    }
    isVisibleDuration.value = false; // 길찾기 초기화
  },
  { immediate: true }
);

// image
const fetchRandomImage = async () => {
  const imageUrl = await fetchUnsplashImage("house");
  props.clickedItem.image = imageUrl || "/images/default-property-image.jpg";
  loadingImage.value = false;
}
</script>

<template>
  <div class="sidebar" v-if="isVisible">
    <!-- 닫기 버튼 -->
    <button class="close-button" @click="closeDetail">
      <font-awesome-icon :icon="['fas', 'x']" />
    </button>
    <button class="bookmark-button" @click="onClickBookmark">
      <font-awesome-icon :icon="['far', 'heart']" v-if="!bookmarked" />
      <font-awesome-icon :icon="['fas', 'heart']" v-if="bookmarked" />
    </button>

    <!-- 스크롤 가능한 콘텐츠 -->
    <div class="content">
      <div class='image-section'>
        <!-- 이미지 섹션 -->
        <div v-if="loadingImage" class="loading-placeholder">
          <font-awesome-icon :icon="['fas', 'spinner']" />
        </div>
        <img
          v-else
          :src="clickedItem.image || '/images/default-property-image.jpg'"
          alt="clickedItem Image"
          class="clickedItem-image"
        />
      </div>
      <!-- 거래 내역 그래프 -->
      <HouseDealGraph :dealList="localDealList" />

      <!-- 길찾기 버튼 -->
      <div class="navigate-button">
        <button @click="onClickDuration">길찾기</button>
      </div>

      <div class="duration-container" v-if="isVisibleDuration">
        <HouseDealDuration
          :lat="clickedItem.latitude"
          :lng="clickedItem.longitude"
        />
      </div>

      <!-- 거래 기록 섹션 -->
      <div class="record-section">
        <div class="record-header">
          <h3>거래 기록</h3>
          <select
            id="filter-select"
            v-model="selectedSortFilter"
            @change="onSortChange"
          >
            <option value="date-new">최신순</option>
            <option value="date-old">오래된순</option>
            <option value="price-high">높은거래가순</option>
            <option value="price-low">낮은거래가순</option>
          </select>
        </div>

        <ul class="record-list">
          <li
            v-for="record in localDealList"
            :key="record.id"
            class="record-item"
          >
            <p>
              <strong>거래일시:</strong> {{ record.dealYear }}.{{
                record.dealMonth
              }}.{{ record.dealDay }}
            </p>
            <p><strong>금액(만원):</strong> {{ record.dealAmount }}</p>
            <p><strong>층:</strong> {{ record.floor }}</p>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* .sidebar: 사이드바의 전체 스타일 */
.sidebar {
  position: relative;
  width: 300px;
  height: 100%; /* 화면 전체 높이 */
  background-color: rgba(255, 255, 255, 0.9);
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  border-left: 1px solid #ddd;
  display: flex;
  flex-direction: column;
}

.content {
  flex-grow: 1; /* 남은 공간을 차지 */
  overflow-y: auto; /* 세로 스크롤 활성화 */
}

/* .image-section: 이미지 섹션 */
.image-section img {
  width: 100%; /* .sidebar의 width에 맞춤 */
  height: 180px; /* height를 180px로 고정 */
  object-fit: cover; /* 비율을 유지하며 영역에 맞게 조정 */
}

/* .navigate-button: 길찾기 버튼 */
.navigate-button {
  padding: 10px;
  border-bottom: 1px solid #ddd;
  text-align: center;
}

.navigate-button button {
  width: 100%;
  padding: 10px;
  font-size: 1rem;
  background-color: #4e4e4e;
  color: white;
  border: none;
  cursor: pointer;
  border-radius: 5px;
}

.navigate-button button:hover {
  background-color: #4e4e4e;
}

.duration-container {
  width: 100%;
}


.close-button,
.bookmark-button {
  position: absolute;
  background-color: #ffffff;
  border: 1px solid #ddd;
  border-radius: 50%;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1rem;
  cursor: pointer;
  z-index: 11;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2); /* 부드러운 그림자 추가 */
  transition: box-shadow 0.2s ease, background-color 0.2s ease; /* 부드러운 전환 효과 */
}

.close-button {
  top: 10px;
  right: 10px;
}

.bookmark-button {
  top: 50px; /* 닫기 버튼 아래에 위치 */
  right: 10px;
}

.close-button:hover,
.bookmark-button:hover {
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2); /* hover 시 그림자 강조 */
  background-color: #f9f9f9; /* hover 시 약간의 색상 변화 */
}


/* .record-section: 거래 기록 */
.record-section {
  flex-grow: 1;
  padding: 15px;
  box-sizing: border-box;
  background-color: transparent; /* 섹션 배경 없음으로 설정 */
}

.record-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 15px;
}

.record-header h3 {
  font-size: 1.2rem;
  font-weight: bold;
  color: #222222; /* 더 짙은 텍스트 색상 */
  margin: 0;
}

#filter-select {
  width: auto;
  max-width: 130px;
  font-size: 0.9rem;
  padding: 6px 10px;
  border: 1px solid #cccccc;
  border-radius: 4px;
  background-color: #ffffff;
  appearance: none;
  cursor: pointer;
  transition: border-color 0.2s, box-shadow 0.2s;
}

#filter-select:hover {
  border-color: #555555; /* 드롭다운 강조 */
}

.record-list {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 16px; /* 항목 간 간격 */
}

.record-item {
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: 4px; /* 항목 내 텍스트 간 간격 */
  border-left: 3px solid #4e4e4e; /* 왼쪽에 포인트 색상 라인 */
  padding-left: 12px; /* 라인과 텍스트 간 간격 */
}

/* 항목 내용 텍스트 */
.record-item p {
  margin: 0;
  font-size: 0.95rem;
  color: #444444; /* 일반 텍스트 색상 */
  line-height: 1.5; /* 가독성을 위한 줄 간격 */
}

.record-item strong {
  font-weight: bold;
  color: #4e4e4e; /* 강조된 텍스트 색상 */
}

/* 리스트 섹션 분리선 (선택적) */
.record-list::after {
  content: "";
  display: block;
  margin-top: 15px;
  height: 1px;
  background-color: #eaeaea;
}
</style>

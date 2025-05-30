<script setup>
import { onMounted, ref } from 'vue'
import SearchBox from "@/components/common/SearchBox.vue";
import VSelect from "@/components/common/VSelect.vue";
import { getFilter, searchKeyword } from "@/api/search";
import { useHouseDealStore } from '@/stores/houseDealStore';
import SearchBoxResult from '../common/SearchBoxResult.vue';
import {toast} from "vue3-toastify";

const props = defineProps({
    searchOption: Object
})

const houseDealStore = useHouseDealStore();

const sidoOptions = ref([]);
const gugunOptions = ref([]);
const dongOptions = ref([]);

const selectedSido = ref("");
const selectedGugun = ref("");
const selectedDong = ref("");

const isLoaded = ref(false);
const result = ref();

onMounted(() => {

    // 지역을 선택한 값이 있으면 해당 값으로 초기화
    getSidoFilter("").then(() => {
        if(houseDealStore.searchOption.dongCode) {
            const { dongCode } = houseDealStore.searchOption;
            selectedSido.value = dongCode.slice(0,2) + "00000000";
            selectedGugun.value = dongCode.slice(0,5) + "00000";
            selectedDong.value = dongCode;

            getGugunFilter(selectedSido.value).then(() => {
                getDongFilter(selectedGugun.value);
            })
        }
    })
})

const onSearch = () => {
    searchKeyword(
        {
            sidoCode: selectedSido.value.slice(0,2),
            gugunCode: selectedGugun.value.slice(0,5),
            dongCode: selectedDong.value.slice(5, 10),
            keyword: houseDealStore.keyword
        },
        ({ data }) => {
            result.value = {
                houses: data.houses,
                regions: data.regions
            };
            isLoaded.value = true;
        },
        (error) => {
            toast.error(error)
        }
    )
}

const onSelectSido = (code) => {
    selectedSido.value = code;
    getGugunFilter(code);
}

const onSelectGugun = (code) => {
    selectedGugun.value = code;
    getDongFilter(code);
}

const onSelectDong = (code) => {
    selectedDong.value = code;
}

// then() 사용하기 위해 promise 반환
const getSidoFilter = (code) => {
    return new Promise((resolve, reject) => {
        getFilter(
            {
                param: code
            },
                ({ data }) => {
                sidoOptions.value = data.map(({ dongCode, sidoName }) => ({ value: dongCode, label: sidoName}));
                sidoOptions.value.unshift({ value: "00", label: "전체" })
                resolve();
            },
            (error) => {
                    toast.error(error);
                    reject(error);
            }
        );
    });
}

const getGugunFilter = (code) => {
    return new Promise((resolve, reject) => {
        getFilter(
            {
                param: code.slice(0, 2)
            },
            ({ data }) => {
                gugunOptions.value = data
                    .filter(item => item.gugunName !== null)
                    .map(({ dongCode, gugunName }) => ({ value: dongCode, label: gugunName }));
                gugunOptions.value.unshift({ value: "000", label: "전체" })
                resolve();
            },
            (error) => {
                toast.error(error);
                reject(error);
            }
        )

    })
}

const getDongFilter = (code) => {
    getFilter(
        {
            param: code.slice(0, 5)
        },
        ({ data }) => {
            dongOptions.value = data
                .filter(item => item.dongName !== null)
                .map(({ dongCode, dongName }) => ({ value: dongCode, label: dongName }));
            dongOptions.value.unshift({ value: "00000", label: "전체" });
        }
    )
}

const onCloseSearchBox = () => {
    isLoaded.value = false;
}

</script>

<template>
    <div class='housedeal-filter'>
        <SearchBox @onSearch='onSearch'/>
        <VSelect :selectOption=sidoOptions :selectedOption='selectedSido' @onKeySelect='onSelectSido'/>
        <VSelect :selectOption=gugunOptions :selectedOption='selectedGugun.slice(2, 5) == "000" ? "전체" : selectedGugun' @onKeySelect='onSelectGugun' />
        <VSelect :selectOption=dongOptions :selectedOption='selectedDong.slice(-5) == "00000" ? "전체" : selectedDong' @onKeySelect='onSelectDong' />
        <SearchBoxResult v-if="isLoaded" :result="result" @onResultClicked="isLoaded = false" class='search-box-result' @closeSearchBox='onCloseSearchBox'/>
    </div>
</template>

<style scoped>
.housedeal-filter {
    margin-left: 50px; /* 원하는 여백 유지 */
    position: absolute; /* 고정 위치로 설정 */
    top: 20px; /* 원하는 위치로 조정 */
    left: 20px; /* 원하는 위치로 조정 */
    width: 50%; /* 필터의 너비 */
    z-index: 20; /* MapResult보다 높게 설정 */
    background-color: rgba(255, 255, 255, 0.9); /* 반투명 배경으로 MapResult 일부 보이게 설정 */
    box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1); /* 필터에 그림자 추가 */
    padding: 10px; /* 내부 여백 추가 */
    border-radius: 8px; /* 둥근 모서리 추가 */
}
</style>

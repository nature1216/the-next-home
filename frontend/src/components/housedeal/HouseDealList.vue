<script setup>
import { ref, onMounted } from 'vue'
import HouseDealItem from "./item/HouseDealItem.vue";
import { defineProps, watch, defineEmits } from "vue";
import PageNavigation from "../common/PageNavigation.vue";

const props = defineProps({
    list : Array,
    total: Number
})

const emit = defineEmits([
    'onItemClick'
])

const result = ref([]);
const total = ref(props.total);

onMounted(() => {
})

watch(() => props.list,
    (newList) => {
        result.value = newList;
})

watch(
    () => props.total,
    (newVal) => {
        total.value = newVal;
    }
)

const onClick = (item) => {
    emit('onItemClick', item);
}

</script>

<template>
    <div class="housedeal-list">
        <div class="list-container">
            <HouseDealItem
                @click="onClick(item)"
                v-for="item in result"
                :key="item.no"
                :item="item"
            />
        </div>
        <PageNavigation class="pagination" :total="total" />
    </div>
</template>

<style scoped>
.housedeal-list {
    display: flex;
    flex-direction: column;
    height: 100%;
}

.list-container {
    margin-left: 20px;
    flex: 1; /* 리스트 컨테이너가 남은 공간을 차지하도록 */
    overflow-y: auto; /* 스크롤 가능하도록 */
}

.pagination {
    position: sticky;
    bottom: 0;
    background-color: transparent;
    margin: 0 auto; /* 부모의 가로 가운데 정렬 */
    width: 100%; /* 부모(`housedeal-list`)의 너비를 상속 */
    display: flex; /* Flexbox로 내부 정렬 */
    justify-content: center; /* 내부 콘텐츠 가로 중앙 정렬 */
}


</style>

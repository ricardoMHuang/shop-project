<template>
  <div class="filter-buttons">
    <div class="filter-button-group">
      <button
          v-for="btn in mergedButtons"
          :key="btn.status"
          :class="['filter-button', { active: currentStatus === btn.status, [btn.status]: true }]"
          @click="currentStatus = btn.status"
      >
        <i :class="btn.icon"></i>
        <span>{{ btn.label }}</span>
      </button>
    </div>
  </div>

  <div class="shop-container">
    <div
        class="shop-card"
        v-for="shop in filteredShops"
        :key="shop.id"
        :class="shop.status"
        @click="openDialog(shop)"
    >
      <h2>{{ shop.name }}</h2>
      <p>{{ shop.address }}</p>
      <p>{{ shop.phone }}</p>
    </div>

    <!-- 弹窗，内容改为插槽 -->
    <el-dialog
        title="店铺详情"
        v-model:visible="showDialog"
        width="400px"
        @close="closeDialog"
    >
      <slot name="dialog" :shop="selectedShop" />
      <template #footer>
        <el-button @click="closeDialog">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const props = defineProps({
  shops: {
    type: Array,
    required: true
  },
  buttons: {
    type: Array,
    default: () => [
      { status: 'all', label: '所有店铺', icon: 'el-icon-menu' },
      { status: 'normal', label: '正常店铺', icon: 'el-icon-check' },
      { status: 'expiring', label: '即将到期', icon: 'el-icon-warning' },
      { status: 'expired', label: '已过期', icon: 'el-icon-error' },
    ]
  }
})

const showDialog = ref(false)
const selectedShop = ref(null)
const currentStatus = ref('all')

// 支持父组件自定义按钮
const mergedButtons = computed(() => props.buttons)

// 过滤逻辑
const filteredShops = computed(() => {
  return currentStatus.value === 'all'
      ? props.shops
      : props.shops.filter(shop => shop.status === currentStatus.value)
})

const openDialog = shop => {
  selectedShop.value = shop
  showDialog.value = true
}

const closeDialog = () => {
  showDialog.value = false
  selectedShop.value = null
}
</script>

<style scoped>
/* 与之前一致，可保留 */
.shop-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 20px;
  padding: 20px;
}

.shop-card {
  width: 140px;
  height: 200px;
  padding: 20px;
  border: 1px solid rgba(255, 255, 255, 0.4);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  box-shadow: 0 10px 40px rgba(31, 38, 135, 0.1);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.shop-card.normal {
  background-color: #4CAF50;
}

.shop-card.expiring {
  background-color: #FFC107;
}

.shop-card.expired {
  background-color: #F44336;
}

.shop-card:hover {
  transform: translateY(-5px) scale(1.03);
  box-shadow: 0 15px 45px rgba(31, 38, 135, 0.2);
}

.filter-buttons {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.filter-button-group {
  display: inline-flex;
  background: #f2f2f2;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.filter-button {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 10px 20px;
  border: none;
  background: transparent;
  cursor: pointer;
  font-weight: 500;
  color: #555;
  transition: all 0.3s ease;
}

.filter-button:hover {
  background: rgba(0, 0, 0, 0.05);
}

.filter-button.active {
  color: #fff;
}

.filter-button.active.all {
  background: linear-gradient(135deg, #409EFF, #66b1ff);
}

.filter-button.active.normal {
  background: linear-gradient(135deg, #67C23A, #85d36b);
}

.filter-button.active.expiring {
  background: linear-gradient(135deg, #E6A23C, #f0b456);
}

.filter-button.active.expired {
  background: linear-gradient(135deg, #F56C6C, #f78787);
}

.filter-button i {
  font-size: 16px;
}

.filter-button span {
  font-size: 14px;
}

.filter-button-group .filter-button + .filter-button {
  border-left: 1px solid #ddd;
}
</style>

<template>
  <aside :class="{ collapsed: isCollapsed }">

    <nav>
      <router-link to="/dashboard" class="nav-item" active-class="active" title="数据分析">
        <DataAnalysis />
        <span v-if="!isCollapsed">数据分析</span>
      </router-link>
      <router-link to="/shops" class="nav-item" active-class="active" title="店铺管理">
        <Grid />
        <span v-if="!isCollapsed">店铺管理</span>
      </router-link>
    </nav>

    <button class="collapse-btn" @click="isCollapsed = !isCollapsed" :title="isCollapsed ? '展开' : '收起'">
      <ArrowRight v-if="isCollapsed" />
      <ArrowLeft v-else />
    </button>
  </aside>
</template>

<script setup>
import { ref } from 'vue'
// 从图标包导入需要用的图标组件
import { DataAnalysis, Grid,ArrowLeft, ArrowRight } from '@element-plus/icons-vue'
const isCollapsed = ref(false)
</script>

<style scoped>
aside {
  background: #001529;
  color: #fff;
  width: 200px;
  transition: width 0.3s ease;
  display: flex;
  flex-direction: column;
  align-items: center;
}
aside.collapsed {
  width: 60px;
}

nav {
  flex: 1;
  display: flex;
  flex-direction: column;
  width: 100%;
}

.nav-item {
  color: #fff;
  text-decoration: none;
  padding: 15px;
  display: flex;
  align-items: center;
  gap: 8px;
  justify-content: center;
}

.nav-item:hover {
  background: rgba(255, 255, 255, 0.1);
}

.nav-item.active {
  background: rgba(255, 255, 255, 0.2);
}

.collapse-btn {
  background: none;
  border: none;
  color: #fff;
  margin: 20px 0;
  cursor: pointer;
  font-size: 20px;
  display: flex;
  justify-content: center;
  width: 100%;
}
.nav-item svg,
.collapse-btn svg {
  width: 20px;
  height: 20px;
}

</style>

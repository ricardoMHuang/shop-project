<template>
  <div class="layout">
    <Sidebar />
    <main>
      <router-view />
    </main>
  </div>
</template>

<script setup>
import Sidebar from './components/Sidebar.vue'
</script>

<style scoped>
.layout {
  display: flex;
  height: 100vh;
}
main {
  flex: 1;
  background: #f5f5f5;
  overflow: auto;
  padding: 20px;
}
</style>

<template>
  <div class="shop-manager">
    <div class="toolbar">
      <el-input
          v-model="searchText"
          placeholder="搜索店铺"
          clearable
          @input="handleSearch"
          class="search-input"
      />
      <el-button type="primary" @click="openDialog('add')">新增店铺</el-button>
    </div>

    <el-table :data="filteredShops" border style="width: 100%; margin-top: 20px;">
      <el-table-column prop="id" label="ID" width="80"/>
      <el-table-column prop="name" label="店铺名称" min-width="150"/>
      <el-table-column prop="location" label="位置" min-width="150"/>
      <el-table-column label="操作" width="200" align="center">
        <template #default="scope">
          <el-button type="primary" size="small" @click="openDialog('edit', scope.row)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 对话框：新增 / 编辑 -->
    <el-dialog
        :title="dialogMode === 'add' ? '新增店铺' : '编辑店铺'"
        v-model="dialogVisible"
        width="400px"
    >
      <el-form :model="form" label-width="80px">
        <el-form-item label="店铺名称">
          <el-input v-model="form.name"/>
        </el-form-item>
        <el-form-item label="位置">
          <el-input v-model="form.location"/>
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// 模拟数据
const shops = ref([
  { id: 1, name: '店铺 A', location: '北京' },
  { id: 2, name: '店铺 B', location: '上海' },
  { id: 3, name: '店铺 C', location: '广州' },
])

const searchText = ref('')
const filteredShops = computed(() => {
  return shops.value.filter(shop =>
      shop.name.includes(searchText.value) || shop.location.includes(searchText.value)
  )
})

const dialogVisible = ref(false)
const dialogMode = ref('add') // 'add' | 'edit'
const form = reactive({ id: null, name: '', location: '' })

// 打开对话框
function openDialog(mode, row) {
  dialogMode.value = mode
  if (mode === 'edit' && row) {
    form.id = row.id
    form.name = row.name
    form.location = row.location
  } else {
    form.id = null
    form.name = ''
    form.location = ''
  }
  dialogVisible.value = true
}

// 提交
function handleSubmit() {
  if (!form.name || !form.location) {
    ElMessage.warning('请填写完整信息')
    return
  }
  if (dialogMode.value === 'add') {
    const newId = shops.value.length ? Math.max(...shops.value.map(s => s.id)) + 1 : 1
    shops.value.push({ id: newId, name: form.name, location: form.location })
    ElMessage.success('新增成功')
  } else if (dialogMode.value === 'edit') {
    const index = shops.value.findIndex(s => s.id === form.id)
    if (index > -1) {
      shops.value[index].name = form.name
      shops.value[index].location = form.location
      ElMessage.success('修改成功')
    }
  }
  dialogVisible.value = false
}

// 删除
function handleDelete(row) {
  ElMessageBox.confirm(`确定要删除 ${row.name} 吗？`, '提示', {
    type: 'warning'
  }).then(() => {
    shops.value = shops.value.filter(s => s.id !== row.id)
    ElMessage.success('已删除')
  })
}

// 搜索（由 computed 实现）
function handleSearch() {
  // 不需要额外操作
}
</script>

<style scoped>
.shop-manager {
  padding: 20px;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-input {
  width: 240px;
}
</style>

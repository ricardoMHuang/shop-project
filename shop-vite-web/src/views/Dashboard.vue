<template>
  <div class="dashboard-container">
    <el-card class="dashboard-card">
      <h2 class="dashboard-title">店铺状态分析</h2>
      <el-button type="primary" @click="toggleChartType('status')" class="toggle-btn">
        {{ chartTypes.status === 'pie' ? '显示柱状图' : '显示饼图' }}
      </el-button>
      <div ref="statusChart" class="chart-container"></div>
    </el-card>

    <el-card class="dashboard-card">
      <h2 class="dashboard-title">店铺类型分布</h2>
      <el-button type="primary" @click="toggleChartType('type')" class="toggle-btn">
        {{ chartTypes.type === 'pie' ? '显示柱状图' : '显示饼图' }}
      </el-button>
      <div ref="typeChart" class="chart-container"></div>
    </el-card>

    <el-card class="dashboard-card rent-card">
      <div class="rent-header">
        <h2 class="dashboard-title">店铺租金收入</h2>
        <div class="rent-controls">
          <el-radio-group v-model="rentViewMode" @change="handleViewModeChange">
            <el-radio-button label="monthly">月度视图</el-radio-button>
            <el-radio-button label="yearly">年度视图</el-radio-button>
          </el-radio-group>
          <el-date-picker
              v-if="rentViewMode === 'monthly'"
              v-model="monthRange"
              type="monthrange"
              range-separator="至"
              start-placeholder="开始月份"
              end-placeholder="结束月份"
              :picker-options="monthPickerOptions"
              @change="updateRentChart"
          />
          <el-date-picker
              v-else
              v-model="yearRange"
              type="yearrange"
              range-separator="至"
              start-placeholder="开始年份"
              end-placeholder="结束年份"
              :picker-options="yearPickerOptions"
              @change="updateRentChart"
          />
        </div>
      </div>
      <div ref="rentChart" class="chart-container"></div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount, watch } from 'vue'
import * as echarts from 'echarts'
import { format, getYear } from 'date-fns'

// 定义所有响应式变量
const statusChart = ref(null)
const typeChart = ref(null)
const rentChart = ref(null)
const chartTypes = ref({ status: 'pie', type: 'pie' })
const rentViewMode = ref('monthly')
const monthRange = ref([new Date(new Date().getFullYear(), 0, 1), new Date()])
const yearRange = ref([new Date(new Date().getFullYear() - 2, 0, 1), new Date()])

// 数据定义
const shopData = [
  { status: '正常', count: 30 },
  { status: '即将到期', count: 10 },
  { status: '已过期', count: 5 }
]

const shopTypeData = [
  { type: '餐饮', count: 15 },
  { type: '零售', count: 20 },
  { type: '服务', count: 8 },
  { type: '娱乐', count: 2 }
]

// 生成租金数据
const rentData = (() => {
  const data = []
  const currentYear = new Date().getFullYear()
  for (let year = currentYear - 2; year <= currentYear; year++) {
    data.push({ date: `${year}`, value: Math.round(500000 + Math.random() * 300000), type: 'year' })
    for (let month = 0; month < 12; month++) {
      if (year === currentYear && month > new Date().getMonth()) break
      data.push({
        date: `${year}-${String(month + 1).padStart(2, '0')}`,
        value: Math.round(30000 + Math.random() * 20000),
        type: 'month'
      })
    }
  }
  return data
})()

// 颜色配置
const statusColors = { '正常': '#67C23A', '即将到期': '#E6A23C', '已过期': '#F56C6C' }
const typeColors = { '餐饮': '#409EFF', '零售': '#FF9A57', '服务': '#9A60B4', '娱乐': '#FF6B81' }
const rentColor = '#36A2EB'

// 图表实例
let statusChartInstance = null
let typeChartInstance = null
let rentChartInstance = null

// 日期选择器配置
const monthPickerOptions = computed(() => ({
  disabledDate: (time) => time > new Date(),
  shortcuts: [
    { text: '最近3个月', onClick(picker) {
        const end = new Date()
        const start = new Date()
        start.setMonth(end.getMonth() - 2)
        picker.$emit('pick', [start, end])
      }},
    { text: '今年至今', onClick(picker) {
        picker.$emit('pick', [new Date(new Date().getFullYear(), 0, 1), new Date()])
      }}
  ]
}))

const yearPickerOptions = computed(() => ({
  disabledDate: (time) => time > new Date(),
  shortcuts: [
    { text: '最近3年', onClick(picker) {
        const end = new Date()
        const start = new Date()
        start.setFullYear(end.getFullYear() - 2)
        picker.$emit('pick', [start, end])
      }}
  ]
}))

// 方法定义
const toggleChartType = (chartName) => {
  chartTypes.value[chartName] = chartTypes.value[chartName] === 'pie' ? 'bar' : 'pie'
}

const handleViewModeChange = () => {
  updateRentChart()
}

const updateRentChart = () => {
  const filteredData = filterRentData()
  rentChartInstance.setOption({
    title: { text: '店铺租金收入' },
    tooltip: {
      trigger: 'axis',
      formatter: (params) => `${params[0].axisValue}<br/>租金收入: ${params[0].data.toLocaleString()}元`
    },
    color: [rentColor],
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: {
      type: 'category',
      data: filteredData.map(item => item.date),
      axisLabel: { rotate: rentViewMode.value === 'monthly' ? 45 : 0 }
    },
    yAxis: { type: 'value', axisLabel: { formatter: '{value} 元' } },
    series: [{
      name: '租金收入',
      type: 'bar',
      barWidth: '60%',
      data: filteredData.map(item => item.value),
      itemStyle: { color: rentColor, borderRadius: [4, 4, 0, 0] },
      label: { show: true, position: 'top', formatter: (params) => params.value.toLocaleString() }
    }]
  })
}

const filterRentData = () => {
  if (rentViewMode.value === 'monthly') {
    if (!monthRange.value) return []
    const [start, end] = monthRange.value
    const startDate = format(start, 'yyyy-MM')
    const endDate = format(end, 'yyyy-MM')
    return rentData
        .filter(item => item.type === 'month' && item.date >= startDate && item.date <= endDate)
        .sort((a, b) => a.date.localeCompare(b.date))
  } else {
    if (!yearRange.value) return []
    const [start, end] = yearRange.value
    const startYear = getYear(start)
    const endYear = getYear(end)
    return rentData
        .filter(item => item.type === 'year' && parseInt(item.date) >= startYear && parseInt(item.date) <= endYear)
        .sort((a, b) => a.date.localeCompare(b.date))
  }
}

const updateCharts = () => {
  // 更新状态图表
  statusChartInstance.setOption({
    title: { text: '店铺状态分析' },
    tooltip: {},
    color: shopData.map(item => statusColors[item.status]),
    xAxis: chartTypes.value.status === 'bar' ? { type: 'category', data: shopData.map(item => item.status) } : undefined,
    yAxis: chartTypes.value.status === 'bar' ? { type: 'value' } : undefined,
    series: [{
      name: '店铺状态',
      type: chartTypes.value.status,
      radius: chartTypes.value.status === 'pie' ? '50%' : undefined,
      data: chartTypes.value.status === 'pie'
          ? shopData.map(item => ({ value: item.count, name: item.status }))
          : shopData.map(item => ({ value: item.count, itemStyle: { color: statusColors[item.status] } })),
      label: { show: true, position: chartTypes.value.status === 'pie' ? 'outside' : 'top' }
    }]
  })

  // 更新类型图表
  typeChartInstance.setOption({
    title: { text: '店铺类型分布' },
    tooltip: {},
    color: shopTypeData.map(item => typeColors[item.type]),
    xAxis: chartTypes.value.type === 'bar' ? { type: 'category', data: shopTypeData.map(item => item.type) } : undefined,
    yAxis: chartTypes.value.type === 'bar' ? { type: 'value' } : undefined,
    series: [{
      name: '店铺类型',
      type: chartTypes.value.type,
      radius: chartTypes.value.type === 'pie' ? '50%' : undefined,
      data: chartTypes.value.type === 'pie'
          ? shopTypeData.map(item => ({ value: item.count, name: item.type }))
          : shopTypeData.map(item => ({ value: item.count, itemStyle: { color: typeColors[item.type] } })),
      label: { show: true, position: chartTypes.value.type === 'pie' ? 'outside' : 'top' }
    }]
  })
}

const resizeCharts = () => {
  statusChartInstance?.resize()
  typeChartInstance?.resize()
  rentChartInstance?.resize()
}

// 生命周期
onMounted(() => {
  statusChartInstance = echarts.init(statusChart.value)
  typeChartInstance = echarts.init(typeChart.value)
  rentChartInstance = echarts.init(rentChart.value)
  updateCharts()
  updateRentChart()
  window.addEventListener('resize', resizeCharts)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', resizeCharts)
  statusChartInstance?.dispose()
  typeChartInstance?.dispose()
  rentChartInstance?.dispose()
})

// 监听器
watch(chartTypes, updateCharts, { deep: true })
</script>

<style scoped>
/* 流动式布局容器 */
.dashboard-container {
  display: flex;
  flex-wrap: wrap;
  gap: 24px;
  padding: 20px;
  justify-content: center;
}

/* 卡片基础样式 */
.dashboard-card {
  flex: 1 1 400px; /* 基础宽度400px，可伸缩 */
  min-width: 0; /* 防止内容溢出 */
  max-width: 800px;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  background: #fff;
}

.dashboard-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
}

/* 租金卡片特殊样式 */
.dashboard-card.rent-card {
  flex: 2 1 800px; /* 更大的伸缩基准 */
}

.dashboard-title {
  text-align: center;
  margin: 0 0 20px;
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.toggle-btn {
  display: block;
  margin: 0 auto 20px;
  width: 120px;
}

.chart-container {
  width: 100%;
  height: 350px;
  margin-top: 10px;
}

/* 租金控制区域 */
.rent-header {
  margin-bottom: 20px;
}

.rent-controls {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  justify-content: center;
  align-items: center;
}

.rent-controls .el-date-picker {
  flex: 1;
  min-width: 280px;
}

/* 响应式调整 */
@media (max-width: 992px) {
  .dashboard-card {
    flex: 1 1 350px;
  }

  .dashboard-card.rent-card {
    flex: 1 1 100%;
  }
}

@media (max-width: 768px) {
  .dashboard-container {
    padding: 15px;
    gap: 16px;
  }

  .dashboard-card {
    flex: 1 1 100%;
    padding: 20px;
  }

  .rent-controls {
    flex-direction: column;
  }

  .rent-controls .el-date-picker {
    width: 100%;
    min-width: auto;
  }

  .chart-container {
    height: 300px;
  }
}

@media (max-width: 480px) {
  .dashboard-title {
    font-size: 16px;
  }

  .toggle-btn {
    width: 100px;
    padding: 8px 12px;
    font-size: 12px;
  }
}
</style>
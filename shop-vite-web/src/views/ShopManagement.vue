<template>
  <ShopCards :shops="shops">
    <template #dialog="{ shop }">
      <p><strong>名称:</strong> {{ shop?.name }}</p>
      <p><strong>地址:</strong> {{ shop?.address }}</p>
      <p><strong>电话:</strong> {{ shop?.phone }}</p>
    </template>
  </ShopCards>
</template>

<script setup>
import ShopCards from '../components/ShopCards.vue'

const shops = [
  { id: 1, name: 'Shop 1', address: 'Address 1', phone: 'Phone 1', status: 'normal' },
  { id: 2, name: 'Shop 2', address: 'Address 2', phone: 'Phone 2', status: 'expiring' },
  { id: 3, name: 'Shop 3', address: 'Address 3', phone: 'Phone 3', status: 'expired' },
  { id: 4, name: 'Shop 4', address: 'Address 4', phone: 'Phone 4', status: 'normal' },
  { id: 5, name: 'Shop 5', address: 'Address 5', phone: 'Phone 5', status: 'expiring' },
  { id: 6, name: 'Shop 6', address: 'Address 6', phone: 'Phone 6', status: 'expired' },
  { id: 7, name: 'Shop 7', address: 'Address 7', phone: 'Phone 7', status: 'normal' },
  { id: 8, name: 'Shop 8', address: 'Address 8', phone: 'Phone 8', status: 'expiring' },
  { id: 9, name: 'Shop 9', address: 'Address 9', phone: 'Phone 9', status: 'expired' },
  { id: 10, name: 'Shop 10', address: 'Address 10', phone: 'Phone 10', status: 'normal' },
  { id: 11, name: 'Shop 11', address: 'Address 11', phone: 'Phone 11', status: 'expiring' },
]
</script>

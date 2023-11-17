import request from '@/utils/request'

export function gettestvariablesList(params) {
  return request({
    url: '/testvariables',
    method: 'get',
    params
  })
}

export function gettestvariablesnum() {
  return request({
    url: '/testvariables/gettestvariablesnum',
    method: 'get'
  })
}

export function gettestvariablesallList() {
  return request({
    url: '/testvariables/getvariableslist',
    method: 'get'
  })
}

export function search(testvariablesForm) {
  return request({
    url: '/testvariables/search',
    method: 'post',
    data: testvariablesForm
  })
}

export function addtestvariables(testvariablesForm) {
  return request({
    url: '/testvariables',
    method: 'post',
    data: testvariablesForm
  })
}

export function findtestvariablesbycaseid(testplanForm) {
  return request({
    url: '/testvariables/findtestvariablesbycaseid',
    method: 'post',
    data: testplanForm
  })
}

export function updatetestvariables(testvariablesForm) {
  return request({
    url: '/testvariables/detail',
    method: 'put',
    data: testvariablesForm
  })
}

export function removetestvariables(testvariablesId) {
  return request({
    url: '/testvariables/' + testvariablesId,
    method: 'delete'
  })
}

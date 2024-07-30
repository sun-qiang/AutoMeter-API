import request from '@/utils/request'

export function getapireportstaticsList(params) {
  return request({
    url: '/performancereport/thread/getplanstatics',
    method: 'get',
    params
  })
}

export function search(apireportstaticsForm) {
  return request({
    url: '/performancereport/thread/search',
    method: 'post',
    data: apireportstaticsForm
  })
}

export function getperformancereportthreadtimedatas(apireportstaticsForm) {
  return request({
    url: '/performancereport/thread/gettimedatas',
    method: 'post',
    data: apireportstaticsForm
  })
}

export function getperformancereportthreaddatas(apireportstaticsForm) {
  return request({
    url: '/performancereport/thread/gettimethreaddatas',
    method: 'post',
    data: apireportstaticsForm
  })
}

export function gettpsxdatas(apireportstaticsForm) {
  return request({
    url: '/performancereport/thread/gettpsxdatas',
    method: 'post',
    data: apireportstaticsForm
  })
}

export function gettpsydatas(apireportstaticsForm) {
  return request({
    url: '/performancereport/thread/gettpsydatas',
    method: 'post',
    data: apireportstaticsForm
  })
}

export function getresponetimexdatas(apireportstaticsForm) {
  return request({
    url: '/performancereport/thread/getresponetimexdatas',
    method: 'post',
    data: apireportstaticsForm
  })
}

export function getresponetimeydatas(apireportstaticsForm) {
  return request({
    url: '/performancereport/thread/getresponetimeydatas',
    method: 'post',
    data: apireportstaticsForm
  })
}

export function getstaticsreportstatics(apireportstaticsForm) {
  return request({
    url: '/performancereport/thread/getstaticsreportstatics',
    method: 'post',
    data: apireportstaticsForm
  })
}

export function addapireportstatics(apireportstaticsForm) {
  return request({
    url: '/performancereport/thread',
    method: 'post',
    data: apireportstaticsForm
  })
}

export function updateapireportstatics(apireportstaticsForm) {
  return request({
    url: '/performancereport/thread',
    method: 'put',
    data: apireportstaticsForm
  })
}

export function removeapireportstatics(apireportstaticsId) {
  return request({
    url: '/performancereport/thread/' + apireportstaticsId,
    method: 'delete'
  })
}

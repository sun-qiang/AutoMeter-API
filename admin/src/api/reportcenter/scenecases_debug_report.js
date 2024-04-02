import request from '@/utils/request'

export function getapireportList(params) {
  return request({
    url: '/scenecases/debug/report',
    method: 'get',
    params
  })
}

export function findscenecasedebugreportWithName(apireportForm) {
  return request({
    url: '/scenecases/debug/report/search',
    method: 'post',
    data: apireportForm
  })
}

export function findApicasereportWithNameandStatus(apireportForm) {
  return request({
    url: '/scenecases/debug/report/findApicasereportWithNameandStatus',
    method: 'post',
    data: apireportForm
  })
}

export function getstaticsreport(apireportForm) {
  return request({
    url: '/scenecases/debug/report/getstaticsreport',
    method: 'post',
    data: apireportForm
  })
}

export function getfunctioncasestatics(apireportForm) {
  return request({
    url: '/scenecases/debug/report/getfunctioncasestatics',
    method: 'post',
    data: apireportForm
  })
}

export function getfunctionconditionstatics(apireportForm) {
  return request({
    url: '/scenecases/debug/report/getfunctionconditionstatics',
    method: 'post',
    data: apireportForm
  })
}

export function getfunctionCaseSandF(apireportForm) {
  return request({
    url: '/scenecases/debug/report/getfunctionCaseSandF',
    method: 'post',
    data: apireportForm
  })
}

export function addapireport(apireportForm) {
  return request({
    url: '/scenecases/debug/report',
    method: 'post',
    data: apireportForm
  })
}

export function updateapireport(apireportForm) {
  return request({
    url: '/scenecases/debug/report',
    method: 'put',
    data: apireportForm
  })
}

export function removeapireport(apireportId) {
  return request({
    url: '/scenecases/debug/report/' + apireportId,
    method: 'delete'
  })
}

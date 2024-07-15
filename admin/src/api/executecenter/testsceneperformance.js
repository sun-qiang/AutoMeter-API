import request from '@/utils/request'

export function gettestsceneperformanceList(params) {
  return request({
    url: '/testscene/performance',
    method: 'get',
    params
  })
}

export function getstaticsplan(params) {
  return request({
    url: '/testscene/performance/getstaticsplan',
    method: 'get',
    params
  })
}

export function gettestsceneperformancenum(params) {
  return request({
    url: '/testscene/performance/gettestsceneperformancenum',
    method: 'get',
    params
  })
}

export function getallexplanbytype(params) {
  return request({
    url: '/testscene/performance/getallexplanbytype',
    method: 'get',
    params
  })
}

export function getallexplan(params) {
  return request({
    url: '/testscene/performance/getallexplan',
    method: 'get',
    params
  })
}

export function search(testsceneperformanceForm) {
  return request({
    url: '/testscene/performance/search',
    method: 'post',
    data: testsceneperformanceForm
  })
}

export function searchscenetreedata(testsceneperformanceForm) {
  return request({
    url: '/testscene/performance/searchscenetreedata',
    method: 'post',
    data: testsceneperformanceForm
  })
}

export function addtestsceneperformance(testsceneperformanceForm) {
  return request({
    url: '/testscene/performance',
    method: 'post',
    data: testsceneperformanceForm
  })
}

export function testsceneperformance(testsceneperformanceForm) {
  return request({
    url: '/testscene/performance/execplancases',
    method: 'post',
    data: testsceneperformanceForm
  })
}

export function checkplancondition(testsceneperformanceForm) {
  return request({
    url: '/testscene/performance/checkcondition',
    method: 'post',
    data: testsceneperformanceForm
  })
}

export function updatetestsceneperformance(testsceneperformanceForm) {
  return request({
    url: '/testscene/performance/detail',
    method: 'put',
    data: testsceneperformanceForm
  })
}

export function copyscene(apicasesForm) {
  return request({
    url: '/testscene/performance/copyscene',
    method: 'post',
    data: apicasesForm
  })
}

export function getsceneallList(params) {
  return request({
    url: '/testscene/performance/scenes',
    method: 'get',
    params
  })
}

export function updatetestsceneperformancestatus(testsceneperformanceForm) {
  return request({
    url: '/testscene/performance/updatestatus',
    method: 'post',
    data: testsceneperformanceForm
  })
}

export function removetestsceneperformance(testsceneperformanceId) {
  return request({
    url: '/testscene/performance/' + testsceneperformanceId,
    method: 'delete'
  })
}

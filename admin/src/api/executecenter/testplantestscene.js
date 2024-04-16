import request from '@/utils/request'

export function gettestplanList(params) {
  return request({
    url: '/testplan/testscene',
    method: 'get',
    params
  })
}

export function getstaticsplancases(params) {
  return request({
    url: '/testplan/testscene/getstaticsplancases',
    method: 'get',
    params
  })
}

export function addtestplantestscene(sceneForm) {
  return request({
    url: '/testplan/testscene/addplanscene',
    method: 'post',
    data: sceneForm
  })
}

export function updateplanscenenorder(executeplanForm) {
  return request({
    url: '/testplan/testscene/updateplanscenenorder',
    method: 'post',
    data: executeplanForm
  })
}

export function getstaticsplan(params) {
  return request({
    url: '/testplan/testscene/getstaticsplan',
    method: 'get',
    params
  })
}

export function gettestplannum(params) {
  return request({
    url: '/testplan/testscene/gettestplannum',
    method: 'get',
    params
  })
}

export function getallexplanbytype(params) {
  return request({
    url: '/testplan/testscene/getallexplanbytype',
    method: 'get',
    params
  })
}

export function getallexplan(params) {
  return request({
    url: '/testplan/testscene/getallexplan',
    method: 'get',
    params
  })
}

export function search(testplanForm) {
  return request({
    url: '/testplan/testscene/search',
    method: 'post',
    data: testplanForm
  })
}

export function findscenebyexecplanid(testplanForm) {
  return request({
    url: '/testplan/testscene/findscenebyexecplanid',
    method: 'post',
    data: testplanForm
  })
}

export function addtestplan(testplanForm) {
  return request({
    url: '/testplan/testscene',
    method: 'post',
    data: testplanForm
  })
}

export function testplan(testplanForm) {
  return request({
    url: '/testplan/testscene/execplancases',
    method: 'post',
    data: testplanForm
  })
}

export function checkplancondition(testplanForm) {
  return request({
    url: '/testplan/testscene/checkcondition',
    method: 'post',
    data: testplanForm
  })
}

export function updatetestplan(testplanForm) {
  return request({
    url: '/testplan/testscene/detail',
    method: 'put',
    data: testplanForm
  })
}

export function updatetestplanstatus(testplanForm) {
  return request({
    url: '/testplan/testscene/updatestatus',
    method: 'post',
    data: testplanForm
  })
}

export function removetestplanscene(executeplanSceneId) {
  return request({
    url: '/testplan/testscene/' + executeplanSceneId,
    method: 'delete'
  })
}

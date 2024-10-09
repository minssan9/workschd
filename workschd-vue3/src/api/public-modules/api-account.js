import api from "@/api/axios-voyagess.js";

const baesURL = `common/account`

const apiPublicAccount = {
    getTeacherList(fetchQuery) {
        fetchQuery.role = 'EN9DOOR_TEACHER'
        return api.get(`${baesURL}/teacher`, fetchQuery)
    },
    getTeacherById(acconutId) {
        return api.get(`${baesURL}/teacher/${acconutId}`)
    }
}

export default apiPublicAccount
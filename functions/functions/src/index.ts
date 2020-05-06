import * as admin from 'firebase-admin'
import * as functions from 'firebase-functions'
admin.initializeApp()

export const notifyNewLike = functions.firestore.document('/users/{userId}/users-liked/{userLiked}')
    .onCreate(async (snapshot, context) => {

        try {

            const newLike = snapshot.data()

            console.log("userId", context.params.userId)
            console.log("userLiked", context.params.userLiked)

            const payload = {
                data: {
                    displayName: newLike.displayName,
                    eventType: "follow"
                }
            }

            const user = await admin.firestore().doc(`users/${context.params.userId}`).get()

            const userData = user.data()

            console.log("userData", userData)

            return admin.messaging().sendToDevice(userData.instanceToken, payload)

        } catch (err) {

            console.log(err)

        }

    })

export const updateDp = functions.https.onCall(async (data, context) => {

    try {

        const imageUrl = data.imageUrl

        console.log("imageUrl", imageUrl)

        const uid = context.auth.uid

        return admin.firestore().doc(`users/${uid}`).set({
            displayImageUrl: imageUrl
        }, {
            merge: true
        })

    } catch (err) {
        console.log(err)
    }

})

export const getUser = functions.https.onCall(async (data, context) => {

    try {

        const uid = data.uid

        const user = await admin.firestore().doc(`users/${uid}`).get()

        console.log("user:data", user.data());


        return user.data()


    } catch (error) {
        console.log(error)

    }
})

export const getProviders = functions.https.onCall(async (data, context) => {
    try {
        const serviceUid = data.serviceUid

        const serviceSnapshot = await admin.firestore().collection(`services/${serviceUid}/providers`).get()

        const providersUid = serviceSnapshot.docs.map(doc => doc.data())

        console.log("providersUid", providersUid);

        const promises = []

        providersUid.forEach(provider => {
            const userData = admin.firestore().doc(`users/${provider.userUid}`).get().then(user => user.data())
            promises.push(userData)
        })

        const users = await Promise.all(promises)

        console.log("users", users);


        return users


    } catch (error) {
        console.log(error)
    }

})


export const addService = functions.https.onCall(async (data, context) => {
    try {
        const userUid = context.auth.uid

        console.log("userUid", userUid)


        const serviceUid = data.serviceUid

        console.log("serviceUid", serviceUid)


        const promises = []

        const services = admin.firestore()
            .collection(`services/${serviceUid}/providers`).doc(`${userUid}`).set({
                userUid: userUid
            })

        promises.push(services)

        const users = admin.firestore()
            .collection(`users/${userUid}/services/`).doc(`${serviceUid}`).set({
                serviceUid: serviceUid
            })

        promises.push(users)

        console.log("promises", Promise.all(promises))


        return Promise.all(promises)

    } catch (error) {
        console.log(error)

    }
})

export const saveUser = functions.https.onCall(async (data, context) => {

    try {

        return admin.firestore().doc(`users/${data.uid}`).set(data)

    } catch (error) {
        console.log(error)
    }

})
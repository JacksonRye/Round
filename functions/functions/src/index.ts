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
        }, { merge: true })

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

// export const saveUser = functions.https.onCall(async(data, context) => {
    
// })
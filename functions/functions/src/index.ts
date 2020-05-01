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

export const updateProfilePicture =

    functions.https.onCall(async (data, callContext) => {
        const uid = data.uid

        try {

            console.log("uid", uid)

            if (!(typeof uid === 'string') || uid.length === 0) {
                throw new functions.https.HttpsError('invalid-argument', 'The function must be called ' + '\
            the uid of the authenticated user')
            }

            return functions.storage.object().onFinalize(async (object, context) => {
                try {
                    const contentType = object.contentType

                    if (!contentType.startsWith('image/')) {
                        console.log("This is not an image.")
                        return
                    }

                    const mediaLink = object.mediaLink

                    console.log("mediaLink", mediaLink)

                    const user = await admin.firestore().doc(`users/${uid}/`).get()

                    return user.ref.set({
                        displayImageUrl: mediaLink
                    }, { merge: true })

                } catch (err) {
                    console.log(err)
                }
            })
        } catch (err) {
            console.log(err)
        }

    })


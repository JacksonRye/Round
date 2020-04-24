import* as admin from 'firebase-admin'
import * as functions from 'firebase-functions'
admin.initializeApp()

// export const onChijiokeChanged =
// functions.firestore.document('users/ElB4E3ncUyXYP0SRGdO283RFGNe2').onUpdate(change => {
//     const after = change.after.data()
//     const payload = {
//         data: {
//             name: String(after!.displayName)
//         }
//     }
//     return admin.messaging().sendToDevice(after!.instanceToken,
//          payload)
// })

// export const getChijioke = functions.https.onRequest(async(request, response) => {

//     try {
//         const snapshot = await admin.firestore().doc('users/ElB4E3ncUyXYP0SRGdO283RFGNe2').get()
//         const data = snapshot.data()
//         response.send(data)
//     } catch (error) {
//         // Handle the error
//         console.log(error)
//         response.status(500).send(error)
//     }

// });


// export const getBostonArea = 
//     functions.https.onRequest(async(request, response) => {
//         try {
//             const areaSnapshot = await admin.firestore().doc("area/boston").get()
//             const cities = areaSnapshot.data()!.cities

//             const promises = []

//             for (const city in cities) {
//                 const p = admin.firestore().doc(`weather/${city}`).get()
//                 promises.push(p)
//             }

//             const citySnapshots = await Promise.all(promises)
//             const results = []

//             citySnapshots.forEach(citySnap => {
//                 const data = citySnap.data()
//                 data!.city = citySnap.id
//                 results.push(data)
//             })

//             return response.send(results)
//         } catch (error) {
//             console.log(error)
//             response.status(500).send(error)
//         }
        
//     })


// export const getProviders = 
//     functions.https.onRequest(async(request, response) => {
//         try{
//             const providersDocRef = 
//                 await admin.firestore().collection('services/QUvWNChEuGgivUGzB6Ew/providers').listDocuments()

//             const promises = []

//             providersDocRef.forEach(providerDocRef => {
//                 const documentSnapshot = providerDocRef.get()
//                 console.log(documentSnapshot)
//                 promises.push(documentSnapshot)
//             });

//             const documentSnapshots = await Promise.all(promises)

//             const results = []

//             documentSnapshots.forEach(documentSnapshot => {
//                 const data = documentSnapshot.data()
//                 console.log(data)
//                 results.push(data)
//             });

//             response.send(results)

//         } catch(err) {
//             console.log(err)
//             response.status(500).send(err)
//         }


// })


export const notifyNewLike = functions.firestore.document('/users/{userId}/users-liked/{userLiked}')
.onCreate(async(snapshot, context) => {

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

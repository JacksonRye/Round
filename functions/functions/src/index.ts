import* as admin from 'firebase-admin'
import * as functions from 'firebase-functions'
admin.initializeApp()

export const onChijiokeChanged =
functions.firestore.document('users/ElB4E3ncUyXYP0SRGdO283RFGNe2').onUpdate(change => {
    const after = change.after.data()
    const payload = {
        data: {
            name: String(after!.displayName)
        }
    }
    return admin.messaging().sendToDevice(after!.instanceToken,
         payload)
})

export const getChijioke = functions.https.onRequest((request, response) => {
 admin.firestore().doc('users/ElB4E3ncUyXYP0SRGdO283RFGNe2').get()
 .then(snapshot => {
     const data = snapshot.data()
     response.send(data)
 })
 .catch(error => {
     // Handle the error
     console.log(error)
     response.status(500).send(error)
 })
});

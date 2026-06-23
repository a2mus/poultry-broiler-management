# Feature Brief: Cloud Sync & Link Sharing

**Sequence**: #013 of 14
**Category**: :link: Integration
**Priority**: Could Have
**Estimated Complexity**: Large

## Dependencies

- **Requires**: #001 Project Scaffolding & Design System, #002 Project Management, #012 PDF Export & Reporting
- **Unlocks**: None (terminal feature)
- **Parallelizable with**: #014 Arabic Localization

## Feature Description

While the app is designed as an offline-first tool, consultants working across multiple devices or collaborating with farm management teams benefit from cloud data backup and remote report distribution. This feature adds optional Firebase-based cloud synchronization for project data and shareable download links for PDF reports.

Cloud Sync enables users to back up their local Room database to Firebase Firestore when internet connectivity becomes available. This is not a real-time sync requirement — consultants work offline in the field and sync when they return to an area with connectivity. The sync strategy uses a last-write-wins conflict resolution approach based on timestamps. Changes are marked locally with timestamps and queued; a background WorkManager job triggers synchronization when the network is verified.

Before using cloud features, the user must authenticate via Firebase Auth with Email/Password. The app operates fully in anonymous/local mode by default — sign-in is never a barrier to using core design features. Authentication is only prompted when the user explicitly activates cloud backup or link sharing. Once authenticated, Firestore Security Rules ensure that each user can only access their own project records.

Web Link Sharing allows users to upload a generated PDF report to Firebase Cloud Storage and receive a shareable download link. This link can be sent to farmers, partners, or stakeholders via any messaging app. The link provides time-limited access to download the PDF without requiring the recipient to install the app or create an account.

This feature is classified as "Could Have" in the MoSCoW prioritization. The core product delivers full value offline; cloud features enhance convenience but are not essential for the primary on-site design and assessment workflow.

## Derived From

- **Product Spec**: §5.2 Secondary Feature 1 — Cloud Sync & Collaboration (Room-to-Firestore sync, last-write-wins, timestamps)
- **Product Spec**: §5.2 Secondary Feature 2 — Web Link Sharing (upload PDF to Cloud Storage, shareable download link)
- **Product Spec**: §5.3 MoSCoW — Firebase Cloud Sync & Link Sharing (Could Have)
- **Product Spec**: §6.1 Technology Stack (Firebase Firestore, Cloud Storage, Firebase Auth)
- **Product Spec**: §6.2 Data Architecture (sync strategy with timestamps, WorkManager)
- **Product Spec**: §6.3 Authentication & Authorization (anonymous default, email/password for cloud features)
- **Constitution**: Article 4.1 (Auth — anonymous default, Firebase Auth for cloud), Article 4.2 (TLS transit, Firestore security rules), Article 4.4 (Secrets — google-services.json not in public repo), Article 5.2 (dev/prod Firebase environments)

## Acceptance Criteria Summary

- [ ] Users can sign in with Firebase Auth (Email/Password) to enable cloud features
- [ ] App operates fully without sign-in; authentication is only prompted for cloud backup or link sharing
- [ ] Project data syncs from local Room to Firestore using last-write-wins conflict resolution with timestamps
- [ ] Sync occurs automatically via WorkManager when network connectivity is restored
- [ ] Users can upload a PDF report to Firebase Cloud Storage and receive a shareable download link
- [ ] Firestore Security Rules restrict project access to the authenticated user's UID only
- [ ] Sync status is visible to the user (last sync time, pending changes indicator)

## Technical Hints

- Constitution Article 4.1 mandates anonymous/local mode as default — never gate core features behind authentication
- Constitution Article 4.4 requires Firebase config (`google-services.json`) not be stored in the public repository with production keys; API keys go in `local.properties` injected via BuildConfig
- Constitution Article 5.2 defines dev/prod Firebase projects — sync must target the correct project based on build flavor
- The sync architecture should use a `SyncManager` in the Data layer that observes Room changes, queues them with timestamps, and dispatches via WorkManager
- Shareable PDF links should have time-limited access (e.g., 7-day expiry) via Firebase Cloud Storage signed URLs

---

**To create the full specification, run:**
```
/speckit-specify [paste the Feature Description section above]
```

# `:benchmarks`

## Module dependency graph

<!--region graph-->
```mermaid
---
config:
  layout: elk
  elk:
    nodePlacementStrategy: SIMPLE
---
graph TB
  subgraph :core
    direction TB
    :core:analytics[analytics]:::android-library
    :core:common[common]:::jvm-library
    :core:data[data]:::android-library
    :core:database[database]:::android-library
    :core:datastore[datastore]:::android-library
    :core:datastore-proto[datastore-proto]:::android-library
    :core:designsystem[designsystem]:::android-library
    :core:domain[domain]:::android-library
    :core:model[model]:::jvm-library
    :core:navigation[navigation]:::android-library
    :core:network[network]:::android-library
    :core:notifications[notifications]:::android-library
    :core:ui[ui]:::android-library
  end
  subgraph :feature:interests
    direction TB
    :feature:interests:api[api]:::android-library
    :feature:interests:impl[impl]:::android-library
  end
  subgraph :feature:foryou
    direction TB
    :feature:foryou:api[api]:::android-library
    :feature:foryou:impl[impl]:::android-library
  end
  subgraph :feature:bookmarks
    direction TB
    :feature:bookmarks:api[api]:::android-library
    :feature:bookmarks:impl[impl]:::android-library
  end
  subgraph :feature:topic
    direction TB
    :feature:topic:api[api]:::android-library
    :feature:topic:impl[impl]:::android-library
  end
  subgraph :feature:search
    direction TB
    :feature:search:api[api]:::android-library
    :feature:search:impl[impl]:::android-library
  end
  subgraph :feature:settings
    direction TB
    :feature:settings:api[api]:::android-library
  end
  subgraph :sync
    direction TB
    :sync:work[work]:::android-library
  end
  :benchmarks[benchmarks]:::android-test
  :app[app]:::android-application

  :app -.->|baselineProfile| :benchmarks
  :app -.-> :core:analytics
  :app -.-> :core:common
  :app -.-> :core:data
  :app -.-> :core:designsystem
  :app -.-> :core:model
  :app -.-> :core:ui
  :app -.-> :feature:bookmarks:api
  :app -.-> :feature:bookmarks:impl
  :app -.-> :feature:foryou:api
  :app -.-> :feature:foryou:impl
  :app -.-> :feature:interests:api
  :app -.-> :feature:interests:impl
  :app -.-> :feature:search:api
  :app -.-> :feature:search:impl
  :app -.-> :feature:settings:api
  :app -.-> :feature:topic:api
  :app -.-> :feature:topic:impl
  :app -.-> :sync:work
  :benchmarks -.->|testedApks| :app
  :core:data -.-> :core:analytics
  :core:data --> :core:common
  :core:data --> :core:database
  :core:data --> :core:datastore
  :core:data --> :core:network
  :core:data -.-> :core:notifications
  :core:database --> :core:model
  :core:datastore -.-> :core:common
  :core:datastore --> :core:datastore-proto
  :core:datastore --> :core:model
  :core:domain --> :core:data
  :core:domain --> :core:model
  :core:network --> :core:common
  :core:network --> :core:model
  :core:notifications -.-> :core:common
  :core:notifications --> :core:model
  :core:ui --> :core:analytics
  :core:ui --> :core:designsystem
  :core:ui --> :core:model
  :feature:bookmarks:api --> :core:navigation
  :feature:bookmarks:impl -.-> :core:data
  :feature:bookmarks:impl -.-> :core:designsystem
  :feature:bookmarks:impl -.-> :core:ui
  :feature:bookmarks:impl -.-> :feature:bookmarks:api
  :feature:bookmarks:impl -.-> :feature:topic:api
  :feature:foryou:api --> :core:navigation
  :feature:foryou:impl -.-> :core:designsystem
  :feature:foryou:impl -.-> :core:domain
  :feature:foryou:impl -.-> :core:notifications
  :feature:foryou:impl -.-> :core:ui
  :feature:foryou:impl -.-> :feature:foryou:api
  :feature:foryou:impl -.-> :feature:topic:api
  :feature:interests:api --> :core:navigation
  :feature:interests:impl -.-> :core:designsystem
  :feature:interests:impl -.-> :core:domain
  :feature:interests:impl -.-> :core:ui
  :feature:interests:impl -.-> :feature:interests:api
  :feature:interests:impl -.-> :feature:topic:api
  :feature:search:api -.-> :core:domain
  :feature:search:api --> :core:navigation
  :feature:search:impl -.-> :core:designsystem
  :feature:search:impl -.-> :core:domain
  :feature:search:impl -.-> :core:ui
  :feature:search:impl -.-> :feature:interests:api
  :feature:search:impl -.-> :feature:search:api
  :feature:search:impl -.-> :feature:topic:api
  :feature:settings:api -.-> :core:data
  :feature:settings:api -.-> :core:designsystem
  :feature:settings:api -.-> :core:ui
  :feature:topic:api -.-> :core:designsystem
  :feature:topic:api --> :core:navigation
  :feature:topic:api -.-> :core:ui
  :feature:topic:impl -.-> :core:data
  :feature:topic:impl -.-> :core:designsystem
  :feature:topic:impl -.-> :core:ui
  :feature:topic:impl -.-> :feature:topic:api
  :sync:work -.-> :core:analytics
  :sync:work -.-> :core:data
  :sync:work -.-> :core:notifications

classDef android-application fill:#CAFFBF,stroke:#000,stroke-width:2px,color:#000;
classDef android-feature fill:#FFD6A5,stroke:#000,stroke-width:2px,color:#000;
classDef android-library fill:#9BF6FF,stroke:#000,stroke-width:2px,color:#000;
classDef android-test fill:#A0C4FF,stroke:#000,stroke-width:2px,color:#000;
classDef jvm-library fill:#BDB2FF,stroke:#000,stroke-width:2px,color:#000;
classDef unknown fill:#FFADAD,stroke:#000,stroke-width:2px,color:#000;
```

<details><summary>📋 Graph legend</summary>

```mermaid
graph TB
  application[application]:::android-application
  feature[feature]:::android-feature
  library[library]:::android-library
  jvm[jvm]:::jvm-library

  application -.-> feature
  library --> jvm

classDef android-application fill:#CAFFBF,stroke:#000,stroke-width:2px,color:#000;
classDef android-feature fill:#FFD6A5,stroke:#000,stroke-width:2px,color:#000;
classDef android-library fill:#9BF6FF,stroke:#000,stroke-width:2px,color:#000;
classDef android-test fill:#A0C4FF,stroke:#000,stroke-width:2px,color:#000;
classDef jvm-library fill:#BDB2FF,stroke:#000,stroke-width:2px,color:#000;
classDef unknown fill:#FFADAD,stroke:#000,stroke-width:2px,color:#000;
```

</details>
<!--endregion-->
